package com.prxd.shebangs.tool;

import android.util.TypedValue;

import com.prxd.shebangs.app.SheBangsApp;

public class Tool {

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                SheBangsApp.getInstance().getResources().getDisplayMetrics());
    }

    /**
     * 条码必须大于11位，且只能包含字母和数字
     *
     * @param code 条码
     * @return 是否是合格条码
     */
    public static boolean isCodeValid(String code) {
        if (code == null) {
            return false;
        }
        if (code.length() < 11) {
            return false;
        } else {
            return code.matches("^[a-z0-9A-Z]+$");
        }
    }

    /**
     * 货号必须大于11位，且只能包含字母和数字
     *
     * @param GoodsCode 货号
     * @return 是否是合格条码
     */
    public static boolean isGoodsCodeValid(String GoodsCode) {
        if (GoodsCode == null) {
            return false;
        }
        return GoodsCode.matches("^[a-z0-9A-Z]+$");
    }
}
