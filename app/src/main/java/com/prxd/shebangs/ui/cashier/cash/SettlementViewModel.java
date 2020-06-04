package com.prxd.shebangs.ui.cashier.cash;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prxd.shebangs.R;
import com.prxd.shebangs.app.SheBangsApp;
import com.prxd.shebangs.branch.BranchKeeper;
import com.prxd.shebangs.branch.Vip;
import com.prxd.shebangs.common.OperateError;
import com.prxd.shebangs.common.OperateInUserView;
import com.prxd.shebangs.common.OperateResult;
import com.prxd.shebangs.serverInterface.CommandResponse;
import com.prxd.shebangs.serverInterface.CommandTypeEnum;
import com.prxd.shebangs.serverInterface.CommandVo;
import com.prxd.shebangs.serverInterface.HttpHandler;
import com.prxd.shebangs.serverInterface.Invoker;
import com.prxd.shebangs.serverInterface.cash.CashInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettlementViewModel extends ViewModel {

    private ArrayList<GoodsInformation> goodsInformationList;            //销售数据
    private ArrayList<SettlementGroupMsg> settlementGroup;          //结算数据
    private Vip vip;                                                //vip信息
    private String payType;                                         //支付方式
    private int useIntegral;                                        //消费积分
    private int useIntegralDeduction;                               //消费积分可抵扣现金
    private float actualPayment;                                    //实付金额
    private float change;                                           //找零金额
    private float saleTotal;                                        //销售总金额
    private String receiptCode;                                     //销售条码
    private MutableLiveData<OperateResult> settlementChangeResult;    //结算数据转换
    private MutableLiveData<OperateResult> vipOperateResult;          //会员查询结果
    private MutableLiveData<OperateResult> settlementResult;          //结算结果
    private MutableLiveData<OperateResult> exchangeIntegralResult;    //积分兑换结果
    private MutableLiveData<OperateResult> actualPaymentResult;       //实付结果

    public SettlementViewModel() {
        vipOperateResult = new MutableLiveData<>();
        settlementResult = new MutableLiveData<>();
        settlementChangeResult = new MutableLiveData<>();
        exchangeIntegralResult = new MutableLiveData<>();
        actualPaymentResult = new MutableLiveData<>();
    }

    public LiveData<OperateResult> getSettlementResult() {
        return settlementResult;
    }

    public LiveData<OperateResult> getVipOperateResult() {
        return vipOperateResult;
    }

    public LiveData<OperateResult> getSettlementChangeResult() {
        return settlementChangeResult;
    }

    public LiveData<OperateResult> getActualPaymentResult() {
        return actualPaymentResult;
    }

    public LiveData<OperateResult> getExchangeIntegralResult() {
        return exchangeIntegralResult;
    }

    /**
     * 获取找零金额
     *
     * @return 找零金额
     */
    public float getChange() {
        return change;
    }

    /**
     * 获取实付金额
     *
     * @return 实付金额
     */
    public float getActualPayment() {
        return actualPayment;
    }

    /**
     * 设置实付金额
     *
     * @param actualPayment 实付金额
     */
    public void setActualPayment(float actualPayment) {

        if ((actualPayment + (float) (this.useIntegral / 100 * 5)) < this.saleTotal) {
            actualPaymentResult.setValue(new OperateResult(new OperateError(-1, (SheBangsApp.getInstance().getString(R.string.settlement_actualPayment_error)), null)));
        } else {
            this.actualPayment = actualPayment;
            this.change = this.actualPayment + this.useIntegralDeduction - this.saleTotal;
            actualPaymentResult.setValue(new OperateResult(new OperateInUserView(null)));
        }
    }

    /**
     * 设置消费积分---在服务器端查询
     *
     * @param useIntegral 消费积分
     */
    public void setUseIntegral(int useIntegral) {
        if (this.vip.jiFen < useIntegral) {
            exchangeIntegralResult.setValue(new OperateResult(new OperateError(-1, SheBangsApp.getInstance().getString(R.string.settlement_exchange_error), null)));
        } else {
            this.useIntegral = useIntegral;
            this.useIntegralDeduction = this.useIntegral / 100 * 5;
            exchangeIntegralResult.setValue(new OperateResult(new OperateInUserView(null)));
        }
    }

    /**
     * 获取消费积分
     *
     * @return 消费积分
     */
    public int getUseIntegral() {
        return useIntegral;
    }

    /**
     * 获取消费积分可抵扣现金
     *
     * @return 消费积分可抵扣现金
     */
    public int getUseIntegralDeduction() {
        return useIntegralDeduction;
    }

    /**
     * 设置支付方式
     *
     * @param payType 支付方式
     */
    public void setPayType(String payType) {
        this.payType = payType;
    }

    /**
     * 获取vip信息
     *
     * @return vip信息
     */
    public Vip getVip() {
        return vip;
    }

    /**
     * 获取转换后的结算数据
     *
     * @return 转换后的结算数据
     */
    public ArrayList<SettlementGroupMsg> getSettlementGroup() {
        return settlementGroup;
    }

    /**
     * 将销售商品信息转换为结算信息
     *
     * @param goodsInformationList 销售商品信息
     */
    public void transformSaleGoodsInformation(ArrayList<GoodsInformation> goodsInformationList) {
        //保持销售数据
        this.goodsInformationList = goodsInformationList;
        //转换成结算数据
        ArrayList<SettlementGoodsMsg> goodsMsgs = new ArrayList<>();
        SettlementGoodsMsg head = new SettlementGoodsMsg("品名", "数量", "单价");
        int numCount = 0;
        float priceCount = 0;
        for (GoodsInformation goods : goodsInformationList) {
            SettlementGoodsMsg settleGoods = new SettlementGoodsMsg(goods.goodsClassName + "(" + goods.id + ")",
                    String.valueOf(goods.num), new DecimalFormat("###.00").format(goods.salePrice));
            numCount += goods.num;
            priceCount += goods.salePrice;
            goodsMsgs.add(settleGoods);
        }
        SettlementGoodsMsg total = new SettlementGoodsMsg("合计", String.valueOf(numCount), new DecimalFormat("###.00").format(priceCount));
        saleTotal = priceCount;
        settlementGroup = new ArrayList<>();
        SettlementGroupMsg groupMsg = new SettlementGroupMsg(head, total, goodsMsgs);
        settlementGroup.add(groupMsg);
        settlementChangeResult.setValue(new OperateResult(new OperateInUserView(null)));
    }

    /**
     * 通过手机号码，查询会员信息
     *
     * @param tel 电话号码
     */
    public void queryVipInformation(String tel) {
        CommandVo vo = new CommandVo();
        vo.typeEnum = CommandTypeEnum.COMMAND_BRANCH_CASH;
        vo.url = CashInterface.GetUserBasicInfo;
        vo.contentType = HttpHandler.ContentType_APP;
        vo.requestMode = HttpHandler.RequestMode_GET;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("tel", tel);
        parameters.put("userkey", BranchKeeper.getInstance().userKey);
        vo.parameters = parameters;
        Invoker.getInstance().setOnEchoResultCallback(this.callback);
        Invoker.getInstance().exec(vo);
    }

    /**
     * 收银结算提交
     */
    public void submitSale() {
        CommandVo vo = new CommandVo();
        vo.typeEnum = CommandTypeEnum.COMMAND_BRANCH_CASH;
        vo.url = CashInterface.PostSaleData;
        vo.contentType = HttpHandler.ContentType_APP;
        vo.requestMode = HttpHandler.RequestMode_POST;
        Map<String, String> parameters = new HashMap<>();

        try {
            JSONObject jsonObject = new JSONObject();
            receiptCode = "A00" + System.currentTimeMillis();
            jsonObject.putOpt("posCode", receiptCode);
            jsonObject.putOpt("guideId", BranchKeeper.getInstance().onDutyGuide.guideId);
            jsonObject.putOpt("payType", this.payType);
            if (this.vip != null) {
                jsonObject.putOpt("tel", this.vip.tel);
                jsonObject.putOpt("useJiFen", useIntegral);
            } else {
                jsonObject.putOpt("tel", "");
                jsonObject.putOpt("useJiFen", 0);
            }
            JSONArray jsonArray = new JSONArray();
            for (GoodsInformation goods : goodsInformationList) {
                JSONObject item = new JSONObject();
                item.putOpt("id", goods.id);
                item.putOpt("k_j_GoodsClass_Id", goods.k_j_GoodsClass_Id);
                item.putOpt("num", goods.num);
                item.putOpt("totalPrice", goods.totalPrice);
                jsonArray.put(item);
            }
            jsonObject.putOpt("data", jsonArray);
            parameters.put("jsonStr", jsonObject.toString());
            parameters.put("userkey", BranchKeeper.getInstance().userKey);
            vo.parameters = parameters;
            Invoker.getInstance().setOnEchoResultCallback(this.callback);
            Invoker.getInstance().exec(vo);
        } catch (JSONException e) {
            e.printStackTrace();
            settlementResult.setValue(new OperateResult(new OperateError(-1, SheBangsApp.getInstance().getString(R.string.build_json_error), null)));
        }
    }

    /**
     * 查询结果返回接口
     */
    private Invoker.OnExecResultCallback callback = new Invoker.OnExecResultCallback() {
        @Override
        public void execResult(CommandResponse result) {
            switch (result.url) {
                case CashInterface.GetUserBasicInfo:        //获取会员信息
                    if (result.success) {
                        try {
                            vip = new Vip(result.data);
                            vipOperateResult.setValue(new OperateResult(new OperateInUserView(null)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            vipOperateResult.setValue(new OperateResult(new OperateError(-1, SheBangsApp.getInstance().getString(R.string.query_format_error), null)));
                        }
                    } else {
                        vipOperateResult.setValue(new OperateResult(new OperateError(result.code, result.msg, null)));
                    }
                    break;
                case CashInterface.PostSaleData:            //提交销售数据
                    if (result.success) {
                        settlementResult.setValue(new OperateResult(new OperateInUserView(null)));
                    } else {
                        settlementResult.setValue(new OperateResult(new OperateError(result.code, result.msg, null)));
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 获取销售提交数据Bundle
     *
     * @return Bundle
     */
    public Bundle getSubmitSaleData() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("vip", vip);
        bundle.putString("payType", payType);
        bundle.putInt("useIntegral", useIntegral);
        bundle.putFloat("actualPayment", actualPayment);
        bundle.putFloat("change", change);
        bundle.putFloat("saleTotal", saleTotal);
        bundle.putString("receiptCode", receiptCode);
        bundle.putParcelableArrayList("sale", goodsInformationList);
        return bundle;
    }
}
