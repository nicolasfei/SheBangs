package com.prxd.shebangs.serverInterface.background;

import com.prxd.shebangs.serverInterface.AbstractInterface;
import com.prxd.shebangs.serverInterface.Command;
import com.prxd.shebangs.serverInterface.CommandTypeEnum;
import com.prxd.shebangs.serverInterface.CommandVo;

public class BackgroundCommand extends Command {
    @Override
    public String execute(CommandVo vo) {
        return super.firstNode.handleMessage(vo);
    }

    @Override
    protected void buildDutyChain() {
        AbstractInterface postAddRemittance = new AddRemittancePost();
        AbstractInterface setBarCodePrint = new BarCodePrintSet();
        AbstractInterface getBusinessResult = new BusinessResultGet();
        AbstractInterface getCodeByGoods = new CodeByGoodsGet();
        AbstractInterface getCodeByInStock = new CodeByInStockGet();
        AbstractInterface confirmReceipt = new ConfirmReceipt();
        AbstractInterface postEditRemittance = new EditRemittancePost();
        AbstractInterface launchGoods = new LaunchGoods();
        AbstractInterface getLaunchPageList = new LaunchPageListGet();
        AbstractInterface getPageListBackGoods = new PageListBackGoodsGet();
        AbstractInterface getPageListBarCode = new PageListBarCodeGet();
        AbstractInterface getPageListGoods = new PageListGoodsGet();
        AbstractInterface getPageListInStock = new PageListInStockGet();
        AbstractInterface getPageListSale = new PageListSaleGet();
        AbstractInterface getPageListSaleNotID = new PageListSaleNotIDGet();
        AbstractInterface receiveGoods = new ReceiveGoods();
        AbstractInterface getReceivePageList = new ReceivePageListGet();
        AbstractInterface getRemittance = new RemittanceGet();
        AbstractInterface getRemittancePageList = new RemittancePageListGet();

        postAddRemittance.setNextHandler(setBarCodePrint);
        setBarCodePrint.setNextHandler(getBusinessResult);
        getBusinessResult.setNextHandler(getCodeByGoods);
        getCodeByGoods.setNextHandler(getCodeByInStock);

        getCodeByInStock.setNextHandler(confirmReceipt);
        confirmReceipt.setNextHandler(postEditRemittance);
        postEditRemittance.setNextHandler(launchGoods);
        launchGoods.setNextHandler(getLaunchPageList);
        getLaunchPageList.setNextHandler(getPageListBackGoods);

        getPageListBackGoods.setNextHandler(getPageListBarCode);
        getPageListBarCode.setNextHandler(getPageListGoods);
        getPageListGoods.setNextHandler(getPageListInStock);
        getPageListInStock.setNextHandler(getPageListSale);

        getPageListSale.setNextHandler(getPageListSaleNotID);
        getPageListSaleNotID.setNextHandler(receiveGoods);
        receiveGoods.setNextHandler(getReceivePageList);
        getReceivePageList.setNextHandler(getRemittance);
        getRemittance.setNextHandler(getRemittancePageList);

        super.firstNode = postAddRemittance;
    }

    @Override
    public CommandTypeEnum getCommandType() {
        return CommandTypeEnum.COMMAND_BRANCH_BACKGROUND;
    }
}
