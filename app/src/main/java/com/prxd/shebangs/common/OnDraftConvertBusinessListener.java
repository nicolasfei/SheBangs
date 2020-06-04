package com.prxd.shebangs.common;

/**
 * 草稿单转换为业务单监听接口
 */
public interface OnDraftConvertBusinessListener {
    /**
     * 转换成功
     *
     * @param business 业务单
     */
    void convertFinish(BusinessBill business);

    /**
     * 转换失败
     *
     * @param msg 失败msg
     */
    void convertError(String msg);
}
