package com.prxd.shebangs.ui.cashier.cash;

import android.os.Parcel;
import android.os.Parcelable;

import com.prxd.shebangs.serverInterface.JsonKey;

import org.json.JSONException;
import org.json.JSONObject;

public class GoodsInformation implements Parcelable {

    public String id = "";
    public String k_j_GoodsClass_Id = "";
    public String goodsClassName = "";
    public String isDaiMai = "";
    public int num = 0;
    public float salePrice = 0;
    public float totalPrice = 0;
    public int stockTotal = 0;

    public GoodsInformation(){

    }

    public GoodsInformation(String information) {
        try {
            /**
             * 物品信息为json格式
             *         {
             *         "id": "2003217EAE2B",
             *         "k_j_GoodsClass_Id": "170708-8D26BC15",
             *         "goodsClassName": "衣服",
             *         "isDaiMai": "正常",
             *         "num": 1,
             *         "salePrice": 45,
             *         "totalPrice": 45,
             *         "stockTotal": 9
             *         }
             */
            JSONObject json = new JSONObject(information);
            this.id = json.getString(JsonKey.JsonKey_id);
            this.k_j_GoodsClass_Id = json.getString(JsonKey.JsonKey_k_j_GoodsClass_Id);
            this.goodsClassName = json.getString(JsonKey.JsonKey_goodsClassName);
            this.isDaiMai = json.getString(JsonKey.JsonKey_isDaiMai);
            this.num = Integer.parseInt(json.getString(JsonKey.JsonKey_num));
            this.salePrice = Float.parseFloat(json.getString(JsonKey.JsonKey_salePrice));
            this.totalPrice = Float.parseFloat(json.getString(JsonKey.JsonKey_totalPrice));
            this.stockTotal = Integer.parseInt(json.getString(JsonKey.JsonKey_stockTotal));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected GoodsInformation(Parcel in) {
        id = in.readString();
        k_j_GoodsClass_Id = in.readString();
        goodsClassName = in.readString();
        isDaiMai = in.readString();
        num = in.readInt();
        salePrice = in.readFloat();
        totalPrice = in.readFloat();
        stockTotal = in.readInt();
    }

    public static final Creator<GoodsInformation> CREATOR = new Creator<GoodsInformation>() {
        @Override
        public GoodsInformation createFromParcel(Parcel in) {
            return new GoodsInformation(in);
        }

        @Override
        public GoodsInformation[] newArray(int size) {
            return new GoodsInformation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(k_j_GoodsClass_Id);
        dest.writeString(goodsClassName);
        dest.writeString(isDaiMai);
        dest.writeInt(num);
        dest.writeFloat(salePrice);
        dest.writeFloat(totalPrice);
        dest.writeInt(stockTotal);
    }
}
