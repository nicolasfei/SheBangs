package com.prxd.shebangs.common;

/**
 * 草稿单类
 * 对用户开单的草稿的描述
 */
public class DraftBill {

    public String billType;    //单子类型
    public String billID;      //单子ID
    public String content;     //单子数据内容，json格式

    public DraftBill(String billType, String billID, String content) {
        this.billType = billType;
        this.billID = billID;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }
}
