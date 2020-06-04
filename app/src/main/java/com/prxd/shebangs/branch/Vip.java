package com.prxd.shebangs.branch;

import android.os.Parcel;
import android.os.Parcelable;

import com.prxd.shebangs.R;
import com.prxd.shebangs.app.SheBangsApp;

import org.json.JSONException;
import org.json.JSONObject;

public class Vip implements Parcelable {
    public String tel;
    public String userName;
    public String valid;
    public boolean isLock;
    public int jiFen;

    public Vip(String information) throws JSONException {
        JSONObject jsonObject = new JSONObject(information);
        this.tel = jsonObject.getString("tel");
        this.userName = jsonObject.getString("userName");
        this.valid = jsonObject.getString("valid");
        this.isLock = jsonObject.getInt("tel") == 1;
        this.jiFen = jsonObject.getInt("tel");
    }

    protected Vip(Parcel in) {
        tel = in.readString();
        userName = in.readString();
        valid = in.readString();
        isLock = in.readByte() != 0;
        jiFen = in.readInt();
    }

    public static final Creator<Vip> CREATOR = new Creator<Vip>() {
        @Override
        public Vip createFromParcel(Parcel in) {
            return new Vip(in);
        }

        @Override
        public Vip[] newArray(int size) {
            return new Vip[size];
        }
    };

    /**
     * 返回格式化展示VIP信息的字符串
     *
     * @return 格式化展示VIP信息的字符串
     */
    public String getVipInformationFormat() {
        return this.tel + "(" + this.jiFen + SheBangsApp.getInstance().getString(R.string.vip_integral)+")";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tel);
        dest.writeString(userName);
        dest.writeString(valid);
        dest.writeByte((byte) (isLock ? 1 : 0));
        dest.writeInt(jiFen);
    }
}
