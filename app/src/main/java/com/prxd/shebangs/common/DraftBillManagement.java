package com.prxd.shebangs.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.prxd.shebangs.app.SheBangsApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DraftBillManagement {

    private static DraftBillManagement management = new DraftBillManagement();
    private Context context;

    private DraftBillManagement() {
        this.context = SheBangsApp.getInstance();
    }

    public static DraftBillManagement getInstance() {
        return management;
    }

    /**
     * 根据单类型，来获取所有的草稿单
     *
     * @param billType 单类型
     * @return 草稿单list
     */
    public List<DraftBill> getDraftBills(String billType) {
        SharedPreferences preferences = this.context.getSharedPreferences(billType, Context.MODE_PRIVATE);
        HashMap<String, String> map = (HashMap<String, String>) preferences.getAll();
        if (map != null && !map.isEmpty()) {
            List<DraftBill> bills = new ArrayList<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                DraftBill bill = new DraftBill(billType, entry.getKey(), entry.getValue());
                bills.add(bill);
            }
            return bills;
        }
        return null;
    }

    /**
     * 更新bill单子的内容
     *
     * @param billType 单类型
     * @param billID   单ID
     * @param content  单更新内容
     */
    public void updateBill(String billType, String billID, String content) {
        SharedPreferences preferences = this.context.getSharedPreferences(billType, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(billID, content);
        editor.apply();
    }

    /**
     * 增加bill单子的内容
     *
     * @param billType 单类型
     * @param billID   单ID
     * @param content  单更新内容
     */
    public void addBill(String billType, String billID, String content) {
        SharedPreferences preferences = this.context.getSharedPreferences(billType, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(billID, content);
        editor.apply();
    }

    /**
     * 增加bill单子的内容
     *
     * @param draft draft
     */
    public void addBill(DraftBill draft) {
        SharedPreferences preferences = this.context.getSharedPreferences(draft.billType, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(draft.billID, draft.content);
        editor.apply();
    }

    /**
     * 删除bill单子
     *
     * @param billType 单类型
     * @param billID   单ID
     */
    public void deleteBill(String billType, String billID) {
        SharedPreferences preferences = this.context.getSharedPreferences(billType, Context.MODE_PRIVATE);
        if (preferences.contains(billID)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(billID);
            editor.apply();
        }
    }
}
