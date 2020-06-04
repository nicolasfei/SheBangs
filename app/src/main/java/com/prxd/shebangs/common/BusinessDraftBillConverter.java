package com.prxd.shebangs.common;

/**
 * 草稿和业务单子之间的相互转换
 */
public abstract class BusinessDraftBillConverter {
    protected OnDraftConvertBusinessListener listener;

    //业务单转换为草稿单
    public abstract DraftBill convertBusinessToDraft(BusinessBill business);

    //草稿单转换为业务单
    public abstract void convertBusinessFormDraft(DraftBill draft);

    /**
     * 设置监听接口
     *
     * @param listener 监听
     */
    public void setOnDraftConvertBusinessListener(OnDraftConvertBusinessListener listener) {
        this.listener = listener;
    }
}
