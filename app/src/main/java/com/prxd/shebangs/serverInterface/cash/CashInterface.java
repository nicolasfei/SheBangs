package com.prxd.shebangs.serverInterface.cash;

import com.prxd.shebangs.serverInterface.AbstractInterface;

public abstract class CashInterface extends AbstractInterface {
    /**
     * 基本信息
     */
    //获取分店信息
    public final static String GetBranch = AbstractInterface.COMMAND_URL+"api/Branch/GetBranch";
    //获取导购集合信息
    public final static String GetGuideList = AbstractInterface.COMMAND_URL+"api/Branch/GetGuideList";
    //获取支付方式
    public final static String GetPaymentList = AbstractInterface.COMMAND_URL+"api/Branch/GetPaymentList";
    //获取货物类别
    public final static String GetGoodsClass = AbstractInterface.COMMAND_URL+"api/Branch/GetGoodsClass";

    /**
     * 销售
     */
    //获取条码销售信息（用于扫码销售时读取条码信息）
    public final static String GetCodeSaleInfo = AbstractInterface.COMMAND_URL+"api/BarCode/GetCodeSaleInfo";
    //获取每日销售统计数据
    public final static String GetDaySaleCount = AbstractInterface.COMMAND_URL+"api/Sale/GetDaySaleCount";
    //获取小票数据
    public final static String SearchTicket = AbstractInterface.COMMAND_URL+"api/Sale/SearchTicket";
    //获取旧货信息（用于旧货销售）
    public final static String GetNoCodeSaleInfo = AbstractInterface.COMMAND_URL+"api/BarCode/GetNoCodeSaleInfo";
    //获取会员信息（收银时会员可用积分查询）
    public final static String GetUserBasicInfo = AbstractInterface.COMMAND_URL+"api/UserBasic/GetUserBasicInfo";
    //获取积分抵扣金额
    public final static String GetJiFenDiscount = AbstractInterface.COMMAND_URL+"api/UserBasic/GetJiFenDiscount";
    //获取会员信息
    public final static String GetUserBasicTotal = AbstractInterface.COMMAND_URL+"api/UserBasic/GetUserBasicTotal";
    //提交销售数据
    public final static String PostSaleData = AbstractInterface.COMMAND_URL+"api/Sale/PostSaleData";

    /**
     * 分店盘点
     */
    //批量提交盘点数据
    public final static String PostPanDianData = AbstractInterface.COMMAND_URL+"api/PanDian/PostPanDianData";
    //获取条码的盘点信息
    public final static String GetBarCodePanDianInfo = AbstractInterface.COMMAND_URL+"api/BarCode/GetBarCodePanDianInfo";
    //查询分店盘点结果
    public final static String GetBranchPanDianData = AbstractInterface.COMMAND_URL+"api/PanDian/GetBranchPanDianData";
}
