package com.prxd.shebangs.common;

/**
 * 业务单类
 */
public class BusinessBill {

    public String billType;     //单类型
    public String billID;       //单ID
    public String businessTime; //开单时间
    public String billingPerson;        //开单人员

    public static final String INVENTORY_BILL = "INVENTORY_BILL";

    public BusinessBill(String billType, String billID, String businessTime, String billingPerson) {
        this.billType = billType;
        this.billID = billID;
        this.businessTime = businessTime;
        this.billingPerson = billingPerson;
    }
}
