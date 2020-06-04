package com.prxd.shebangs.ui.cashier.cash;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prxd.shebangs.R;
import com.prxd.shebangs.app.SheBangsApp;
import com.prxd.shebangs.branch.BranchKeeper;
import com.prxd.shebangs.common.OperateError;
import com.prxd.shebangs.common.OperateInUserView;
import com.prxd.shebangs.common.OperateResult;
import com.prxd.shebangs.serverInterface.CommandResponse;
import com.prxd.shebangs.serverInterface.CommandTypeEnum;
import com.prxd.shebangs.serverInterface.CommandVo;
import com.prxd.shebangs.serverInterface.HttpHandler;
import com.prxd.shebangs.serverInterface.Invoker;
import com.prxd.shebangs.serverInterface.cash.CashInterface;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CashViewModel extends ViewModel {
    private ArrayList<GoodsInformation> goodsInformationList;
    private MutableLiveData<OperateResult> goodsOperateResult;
    private MutableLiveData<OperateResult> oldGoodsOperateResult;
    private MutableLiveData<OperateResult> removeGoodsResult;
    private float priceTotal = 0;

    public CashViewModel() {
        goodsInformationList = new ArrayList<>();
        goodsOperateResult = new MutableLiveData<>();
        oldGoodsOperateResult = new MutableLiveData<>();
        removeGoodsResult = new MutableLiveData<>();
    }

    public ArrayList<GoodsInformation> getGoodsInformationList() {
        return goodsInformationList;
    }

    public LiveData<OperateResult> getGoodsOperateResult() {
        return goodsOperateResult;
    }

    public LiveData<OperateResult> getOldGoodsOperateResult() {
        return oldGoodsOperateResult;
    }

    public MutableLiveData<OperateResult> getRemoveGoodsResult() {
        return removeGoodsResult;
    }

    /**
     * 通过条码查询商品信息
     *
     * @param goodsCode 商品条码
     */
    public void queryGoodsInformation(String goodsCode) {
        //检查是否重复商品条码
        for (GoodsInformation goods : goodsInformationList) {
            if (goods.id.equals(goodsCode)) {
                goodsOperateResult.setValue(new OperateResult(new OperateError(-1, SheBangsApp.getInstance().getString(R.string.cashier_repeat_item), null)));
                return;
            }
        }
        //发送查询
        CommandVo vo = new CommandVo();
        vo.typeEnum = CommandTypeEnum.COMMAND_BRANCH_CASH;
        vo.url = CashInterface.GetCodeSaleInfo;
        vo.contentType = HttpHandler.ContentType_APP;
        vo.requestMode = HttpHandler.RequestMode_GET;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("code", goodsCode);
        parameters.put("userkey", BranchKeeper.getInstance().userKey);
        vo.parameters = parameters;
        Invoker.getInstance().setOnEchoResultCallback(this.callback);
        Invoker.getInstance().exec(vo);
    }

    /**
     * 添加旧货销售
     *
     * @param old 旧货
     */
    public void addOldGoodsSale(GoodsInformation old) {
        goodsInformationList.add(old);
        priceTotal += old.totalPrice;
        oldGoodsOperateResult.setValue(new OperateResult(new OperateInUserView(null)));
    }

    /**
     * 删除货物
     *
     * @param position position
     */
    public void removeGoods(int position) {
        priceTotal -= goodsInformationList.get(position).totalPrice;
        goodsInformationList.remove(position);
        removeGoodsResult.setValue(new OperateResult(new OperateInUserView(null)));
    }

    /**
     * 查询已添加的商品数量
     *
     * @return 已添加的商品数量
     */
    public int getGoodsCount() {
        return goodsInformationList.size();
    }

    /**
     * 查询已添加的商品数量总价
     *
     * @return 已添加的商品数量总价
     */
    public String getPriceTotal() {
        return new DecimalFormat("###.00").format(priceTotal);
    }

    /**
     * 查询结果返回接口
     */
    private Invoker.OnExecResultCallback callback = new Invoker.OnExecResultCallback() {
        @Override
        public void execResult(CommandResponse result) {
            switch (result.url) {
                case CashInterface.GetCodeSaleInfo:
                    if (result.success) {
                        GoodsInformation item = new GoodsInformation(result.data);
                        goodsInformationList.add(item);
                        priceTotal += item.totalPrice;
                        goodsOperateResult.setValue(new OperateResult(new OperateInUserView(null)));
                    } else {
                        goodsOperateResult.setValue(new OperateResult(new OperateError(result.code, result.msg, null)));
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 开始新的收银
     */
    public void startNewCashier() {
        priceTotal = 0;
        goodsInformationList.clear();
        goodsOperateResult.setValue(new OperateResult(new OperateInUserView(null)));
    }
}
