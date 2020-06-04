package com.prxd.shebangs.serverInterface.background;

import com.prxd.shebangs.serverInterface.AbstractInterface;

public abstract class BackgroundInterface extends AbstractInterface {
    /**
     * 汇款登记
     */
    //新增分店汇款登记
    public final static String PostAddRemittance = AbstractInterface.COMMAND_URL+"api/Remittance/PostAddRemittance";
    //编辑分店汇款登记
    public final static String PostEditRemittance = AbstractInterface.COMMAND_URL+"api/Remittance/PostEditRemittance";
    //根据日期获取当日营业额与积分抵扣金额
    public final static String GetBusinessResult = AbstractInterface.COMMAND_URL+"api/Remittance/GetBusinessResult";
    //根据id获取分店汇款登记信息
    public final static String GetRemittance = AbstractInterface.COMMAND_URL+"api/Remittance/GetRemittance";
    //获取分店汇款登记分页信息
    public final static String GetRemittancePageList = AbstractInterface.COMMAND_URL+"api/Remittance/GetPageList";

    /**
     * 店铺收货
     */
    //获取店铺收货分页信息
    public final static String GetPageListInStock = AbstractInterface.COMMAND_URL+"api/InStock/GetPageList";
    //确认收货
    public final static String ConfirmReceipt = AbstractInterface.COMMAND_URL+"api/InStock/ConfirmReceipt";
    //根据进货记录id查询条码
    public final static String GetCodeByInStock = AbstractInterface.COMMAND_URL+"api/BarCode/GetCodeByInStock";
    //条码打印标记
    public final static String SetBarCodePrint = AbstractInterface.COMMAND_URL+"api/BarCode/SetBarCodePrint";

    /**
     * 店铺返货
     */
    //获取店铺返货分页信息
    public final static String GetPageListBackGoods = AbstractInterface.COMMAND_URL+"api/BackGoods/GetPageList";

    /**
     * 发起调货
     */
    //发起调货
    public final static String LaunchGoods = AbstractInterface.COMMAND_URL+"api/TransferGoods/LaunchGoods";
    //获取分店发起调货分页信息
    public final static String GetLaunchPageList = AbstractInterface.COMMAND_URL+"api/TransferGoods/GetLaunchPageList";

    /**
     * 接收调货
     */
    //接收调货
    public final static String ReceiveGoods = AbstractInterface.COMMAND_URL+"api/TransferGoods/ReceiveGoods";
    //获取分店接收调货分页信息
    public final static String GetReceivePageList = AbstractInterface.COMMAND_URL+"api/TransferGoods/GetReceivePageList";

    /**
     * 旧货销售记录
     */
    //获取旧货销售记录分页信息
    public final static String GetPageListSaleNotID = AbstractInterface.COMMAND_URL+"api/SaleNotID/GetPageList";

    /**
     * 新货销售记录
     */
    //获取新货销售记录分页信息
    public final static String GetPageListSale = AbstractInterface.COMMAND_URL+"api/Sale/GetPageList";

    /**
     * 条码查询
     */
    //获取分店条码数据分页信息
    public final static String GetPageListBarCode = AbstractInterface.COMMAND_URL+"api/BarCode/GetPageList";

    /**
     * 库存统计
     */
    //根据库存记录id查询条码
    public final static String GetCodeByGoods = AbstractInterface.COMMAND_URL+"api/BarCode/GetCodeByGoods";
    //获取店铺库存分页信息
    public final static String GetPageListGoods = AbstractInterface.COMMAND_URL+"api/Goods/GetPageList";
}
