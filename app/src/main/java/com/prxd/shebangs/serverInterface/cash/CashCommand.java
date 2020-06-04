package com.prxd.shebangs.serverInterface.cash;

import com.prxd.shebangs.serverInterface.Command;
import com.prxd.shebangs.serverInterface.CommandTypeEnum;
import com.prxd.shebangs.serverInterface.CommandVo;

public class CashCommand extends Command {
    @Override
    public String execute(CommandVo vo) {
        return super.firstNode.handleMessage(vo);
    }

    @Override
    protected void buildDutyChain() {
        CashInterface getBranch = new BranchQuery();
        CashInterface getGuideList = new GuideListQuery();
        CashInterface getPaymentList = new PaymentListQuery();
        CashInterface getGoodsClass = new GoodsClassQuery();
        CashInterface getCodeSaleInfo = new CodeSaleInfoQuery();
        CashInterface getDaySaleCount = new DaySaleCountQuery();
        CashInterface searchTicket = new SearchTicket();
        CashInterface getNoCodeSaleInfo = new NoCodeSaleInfoQuery();
        CashInterface getUserBasicInfo = new UserBasicInfoQuery();
        CashInterface getJiFenDiscount = new JiFenDiscountQuery();
        CashInterface getUserBasicTotal = new UserBasicTotalQuery();
        CashInterface postSaleData = new SaleDataSubmit();
        CashInterface postPanDianData = new PanDianDataSubmit();
        CashInterface getBarCodePanDianInfo = new BarCodePanDianInfoQuery();
        CashInterface getBranchPanDianData = new BranchPanDianDataQuery();

        getCodeSaleInfo.setNextHandler(getUserBasicInfo);
        getUserBasicInfo.setNextHandler(getGoodsClass);
        getGoodsClass.setNextHandler(postSaleData);
        postSaleData.setNextHandler(getBranch);
        getBranch.setNextHandler(getGuideList);
        getGuideList.setNextHandler(getPaymentList);
        getPaymentList.setNextHandler(getDaySaleCount);
        getDaySaleCount.setNextHandler(searchTicket);
        searchTicket.setNextHandler(getNoCodeSaleInfo);
        getNoCodeSaleInfo.setNextHandler(getJiFenDiscount);
        getJiFenDiscount.setNextHandler(getUserBasicTotal);
        getUserBasicTotal.setNextHandler(postPanDianData);
        postPanDianData.setNextHandler(getBarCodePanDianInfo);
        getBarCodePanDianInfo.setNextHandler(getBranchPanDianData);

        super.firstNode = getCodeSaleInfo;
    }

    @Override
    public CommandTypeEnum getCommandType() {
        return CommandTypeEnum.COMMAND_BRANCH_CASH;
    }
}
