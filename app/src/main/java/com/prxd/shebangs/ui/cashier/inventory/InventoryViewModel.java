package com.prxd.shebangs.ui.cashier.inventory;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prxd.shebangs.R;
import com.prxd.shebangs.app.SheBangsApp;
import com.prxd.shebangs.branch.BranchKeeper;
import com.prxd.shebangs.common.BusinessBill;
import com.prxd.shebangs.common.DraftBill;
import com.prxd.shebangs.common.DraftBillManagement;
import com.prxd.shebangs.common.InputFormState;
import com.prxd.shebangs.common.OnDraftConvertBusinessListener;
import com.prxd.shebangs.common.OperateError;
import com.prxd.shebangs.common.OperateInUserView;
import com.prxd.shebangs.common.OperateResult;
import com.prxd.shebangs.component.datetimepicker.DateFormatUtils;
import com.prxd.shebangs.serverInterface.CommandResponse;
import com.prxd.shebangs.serverInterface.CommandTypeEnum;
import com.prxd.shebangs.serverInterface.CommandVo;
import com.prxd.shebangs.serverInterface.HttpHandler;
import com.prxd.shebangs.serverInterface.Invoker;
import com.prxd.shebangs.serverInterface.cash.CashInterface;
import com.prxd.shebangs.tool.Tool;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryViewModel extends ViewModel implements Invoker.OnExecResultCallback {

    private List<InventoryGoodsInformation> goodsInformationList;
    private String businessTime;                                        //业务时间
    private float goodsTotalPrice;                                      //商品价格合计
    private String draftBillID;                                         //草稿单ID

    private MutableLiveData<InputFormState> inputFormState;             //输入格式是否正确
    private MutableLiveData<OperateResult> setBusinessTimeResult;       //设置业务时间结果
    private MutableLiveData<OperateResult> queryInventoryGoodsResult;   //查询盘点商品信息结果
    private MutableLiveData<OperateResult> deleteInventoryGoodsResult;  //删除盘点商品信息结果
    private MutableLiveData<OperateResult> businessDraftResult;         //盘点单子草稿结果
    private MutableLiveData<OperateResult> loadDraftBillResult;         //加载草稿单结果
    private MutableLiveData<OperateResult> batchSubmitResult;           //批量提交盘点数据结果

    public InventoryViewModel() {
        this.goodsInformationList = new ArrayList<>();
        this.inputFormState = new MutableLiveData<>();
        this.setBusinessTimeResult = new MutableLiveData<>();
        this.queryInventoryGoodsResult = new MutableLiveData<>();
        this.deleteInventoryGoodsResult = new MutableLiveData<>();
        this.businessDraftResult = new MutableLiveData<>();
        this.loadDraftBillResult = new MutableLiveData<>();
        this.batchSubmitResult = new MutableLiveData<>();
    }

    public List<InventoryGoodsInformation> getGoodsInformationList() {
        return goodsInformationList;
    }

    public LiveData<InputFormState> getInputFormState() {
        return inputFormState;
    }

    public LiveData<OperateResult> getQueryInventoryGoodsResult() {
        return queryInventoryGoodsResult;
    }

    public LiveData<OperateResult> getDeleteInventoryGoodsResult() {
        return deleteInventoryGoodsResult;
    }

    public LiveData<OperateResult> getBusinessDraftResult() {
        return businessDraftResult;
    }

    public LiveData<OperateResult> getSetBusinessTimeResult() {
        return setBusinessTimeResult;
    }

    public LiveData<OperateResult> getLoadDraftBillResult() {
        return loadDraftBillResult;
    }

    public LiveData<OperateResult> getBatchSubmitResult() {
        return batchSubmitResult;
    }

    /**
     * 条码输出框发生变化，检查是否格式有错误
     *
     * @param code 条码
     */
    public void codeDataChanged(String code) {
        if (Tool.isCodeValid(code)) {
            inputFormState.setValue(new InputFormState(true));
        } else {
            inputFormState.setValue(new InputFormState(SheBangsApp.getInstance().getString(R.string.code_format_error)));
        }
    }


    /**
     * 根据条码查盘点商品信息
     *
     * @param goodsCode 商品条码
     */
    public void queryInventoryInfoFormCode(String goodsCode) {
        //检查是否重复条码
        for (InventoryGoodsInformation goods : goodsInformationList) {
            if (goods.k_j_BarCode_Id.equals(goodsCode)) {
                queryInventoryGoodsResult.setValue(new OperateResult(new OperateError(0, SheBangsApp.getInstance().getString(R.string.repeat_goods), null)));
                return;
            }
        }
        //查询数据
        CommandVo vo = new CommandVo();
        vo.typeEnum = CommandTypeEnum.COMMAND_BRANCH_CASH;
        vo.url = CashInterface.GetBarCodePanDianInfo;
        vo.contentType = HttpHandler.ContentType_APP;
        vo.requestMode = HttpHandler.RequestMode_GET;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("code", goodsCode);
        parameters.put("userkey", BranchKeeper.getInstance().userKey);
        vo.parameters = parameters;
        Invoker.getInstance().setOnEchoResultCallback(this);
        Invoker.getInstance().exec(vo);
    }

    /**
     * 批量提交盘点数据
     */
    public void batchSubmit() {
        CommandVo vo = new CommandVo();
        vo.typeEnum = CommandTypeEnum.COMMAND_BRANCH_CASH;
        vo.url = CashInterface.PostPanDianData;
        vo.contentType = HttpHandler.ContentType_APP;
        vo.requestMode = HttpHandler.RequestMode_POST;
        JSONArray array = new JSONArray();
        for (InventoryGoodsInformation goods : goodsInformationList) {
            array.put(goods.getSubmitJsonData());
        }
        Map<String, String> parameters = new HashMap<>();
        parameters.put("jsonStr", array.toString());
        parameters.put("userkey", BranchKeeper.getInstance().userKey);
        vo.parameters = parameters;
        Invoker.getInstance().setOnEchoResultCallback(this);
        Invoker.getInstance().exec(vo);
    }

    @Override
    public void execResult(CommandResponse result) {
        switch (result.url) {
            case CashInterface.GetBarCodePanDianInfo:
                if (result.success) {
                    try {
                        InventoryGoodsInformation newGoods = new InventoryGoodsInformation(result.data);
                        goodsInformationList.add(newGoods);
                        goodsTotalPrice += newGoods.salePrice;
                        queryInventoryGoodsResult.setValue(new OperateResult(new OperateInUserView(null)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        queryInventoryGoodsResult.setValue(new OperateResult(new OperateError(0, SheBangsApp.getInstance().getString(R.string.goods_format_error), null)));
                    }
                } else {
                    queryInventoryGoodsResult.setValue(new OperateResult(new OperateError(result.code, result.msg, null)));
                }
                break;
            case CashInterface.PostPanDianData:
                if (result.success) {
                    goodsInformationList.clear();
                    goodsTotalPrice = 0;
                    batchSubmitResult.setValue(new OperateResult(new OperateInUserView(null)));
                } else {
                    batchSubmitResult.setValue(new OperateResult(new OperateError(result.code, result.msg, null)));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 删除商品信息
     *
     * @param position position
     */
    public void deleteInventoryInfoForm(int position) {
        goodsTotalPrice -= goodsInformationList.get(position).salePrice;
        goodsInformationList.remove(position);
        deleteInventoryGoodsResult.setValue(new OperateResult(new OperateInUserView(null)));
    }

    /**
     * 获取商品数量
     *
     * @return 数量
     */
    public int getGoodsNum() {
        return goodsInformationList.size();
    }

    /**
     * 获取商品总价
     *
     * @return 总价
     */
    public String getGoodsTotalPrice() {
        return new DecimalFormat("###.00").format(goodsTotalPrice);
    }

    /**
     * 盘点业务单草稿
     */
    public void businessDraft() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InventoryBusinessBill bill = new InventoryBusinessBill("B" + System.currentTimeMillis(), businessTime, BranchKeeper.getInstance().onDutyGuide.guideName);
                bill.setInventoryGoodsList(goodsInformationList);
                DraftBill draft = new InventoryDraftBillConverter().convertBusinessToDraft(bill);
                if (draft == null) {
                    handler.sendEmptyMessage(1);
                } else {
                    DraftBillManagement.getInstance().addBill(draft);
                    handler.sendEmptyMessage(2);
                }
            }
        }).start();
    }

    private Handler handler = new Handler(SheBangsApp.getInstance().getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:     //保存草稿单失败
                    businessDraftResult.setValue(new OperateResult(new OperateError(0, SheBangsApp.getInstance().getString(R.string.bill_draft_error), null)));
                    break;
                case 2:     //保存草稿单成功
                    businessDraftResult.setValue(new OperateResult(new OperateInUserView(null)));
                    break;
            }
        }
    };

    /**
     * 清空内容
     */
    public void contentClear() {
        this.goodsInformationList.clear();
        this.goodsTotalPrice = 0;
        this.businessTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true); //更新为最新时间
        this.setBusinessTimeResult.setValue(new OperateResult(new OperateInUserView(null)));
        this.queryInventoryGoodsResult.setValue(new OperateResult(new OperateInUserView(null)));
    }

    /**
     * 设置业务时间
     *
     * @param businessTime 业务时间
     */
    public void setBusinessTime(String businessTime) {
        this.businessTime = businessTime;
    }

    /**
     * 获取业务时间
     *
     * @return 业务时间
     */
    public String getBusinessTime() {
        return this.businessTime;
    }

    /**
     * 加载草稿单
     */
    public void loadDraftBill(DraftBill draftBill) {
        this.draftBillID = draftBill.billID;
        InventoryDraftBillConverter converter = new InventoryDraftBillConverter();
        converter.setOnDraftConvertBusinessListener(new OnDraftConvertBusinessListener() {
            @Override
            public void convertFinish(BusinessBill business) {
                InventoryBusinessBill bill = (InventoryBusinessBill) business;
                businessTime = bill.businessTime;
                goodsTotalPrice = bill.getGoodsTotalPrice();
                BranchKeeper.getInstance().setOnDutyGuide(bill.billingPerson);
                //先清空
                if (goodsInformationList.size() > 0) {
                    goodsInformationList.clear();
                }
                goodsInformationList.addAll(bill.getInventoryGoodsList());
                loadDraftBillResult.setValue(new OperateResult(new OperateInUserView(null)));
            }

            @Override
            public void convertError(String msg) {
                loadDraftBillResult.setValue(new OperateResult(new OperateError(0, msg, null)));
            }
        });
        converter.convertBusinessFormDraft(draftBill);
    }
}
