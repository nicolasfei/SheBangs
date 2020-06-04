package com.prxd.shebangs.ui.cashier.inventory;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * "k_j_BarCode_Id": "200318A713BB",
 * "goodsClassName": "牛仔裤",
 * "gId": "A021",
 * "salePrice": 0,
 * "oldGoodsId": "io",
 * "goodsId": "8888888",
 * "inTime": "2020-03-18T17:32:12",
 * "stateClass": "供货商",
 * "isDaiMai": "正常",
 * "getTime": "2020-03-31T15:55:03.8490543+08:00",
 * "stateName": "正常"
 */
public class InventoryGoodsInformation {
    public String k_j_BarCode_Id;
    public String goodsClassName;
    public String gId;
    public float salePrice;
    public String oldGoodsId;
    public String goodsId;
    public String inTime;
    public String stateClass;
    public String isDaiMai;
    public String getTime;
    public String stateName;

    public InventoryGoodsInformation(String data) throws JSONException {
        JSONObject object = new JSONObject(data);
        this.k_j_BarCode_Id = object.getString("k_j_BarCode_Id");
        this.goodsClassName = object.getString("goodsClassName");
        this.gId = object.getString("gId");
        this.salePrice = Float.parseFloat(object.getString("salePrice"));
        this.oldGoodsId = object.getString("oldGoodsId");
        this.goodsId = object.getString("goodsId");
        this.inTime = object.getString("inTime");
        this.stateClass = object.getString("stateClass");
        this.isDaiMai = object.getString("isDaiMai");
        this.getTime = object.getString("getTime");
        this.stateName = object.getString("stateName");
    }

    /**
     * 显示2位小数的价格
     *
     * @return 价格
     */
    public String getFormatSalePrice() {
        return new DecimalFormat("###.00").format(salePrice);
    }

    /**
     * 获取提交数据所需的json
     * @return json
     */
    public JSONObject getSubmitJsonData() {
        JSONObject object = new JSONObject();
        try {
            object.putOpt("k_j_BarCode_Id", k_j_BarCode_Id);
            object.putOpt("getTime", getTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
