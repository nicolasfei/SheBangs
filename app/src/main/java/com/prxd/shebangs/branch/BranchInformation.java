package com.prxd.shebangs.branch;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * {
 * "id": "170708001",
 * "fId": "A001",
 * "xsName": "怦然心动",
 * "name": "测试分店",
 * "password": "34DC9BC32E281359BE",
 * "dzName": "sy",
 * "dzTel": "18224407848",
 * "dzPassword": "34DC9BC32E281359BE",
 * "address": "222",
 * "tsTel": "18224407848",
 * "remark": "",
 * "kufang": "库房2",
 * "k_j_Area_Id": "171208255BA1669B",
 * "danbaoren": "刘莹",
 * "isSpecial": "",
 * "branchType": "直营",
 * "previewPwd": "34DC9BC32E281359BE",
 * "stall": "3档",
 * "k_j_Storage_Id": "1810091460932297",
 * "ssLimit": 1200,
 * "orderPower": 1,
 * "branch_Stage": "高一档",
 * "saleType": "男装店",
 * "province": "",
 * "city": "",
 * "county": "",
 * "town": "",
 * "oldName": "测试分店",
 * "stallB": "1档",
 * "integralTimes": 2
 * }
 */
public class BranchInformation {
    public String id;
    public String fId;
    public String xsName;
    public String name;
    public String password;
    public String dzName;
    public String dzTel;
    public String dzPassword;
    public String address;
    public String tsTel;
    public String remark;
    public String kufang;
    public String k_j_Area_Id;
    public String danbaoren;
    public String isSpecial;
    public String branchType;
    public String previewPwd;
    public String stall;
    public String k_j_Storage_Id;
    public String ssLimit;
    public String orderPower;
    public String branch_Stage;
    public String saleType;
    public String province;
    public String city;
    public String county;
    public String town;
    public String oldName;
    public String stallB;
    public String integralTimes;

    public BranchInformation(String branchInformation) throws JSONException {
            JSONObject information = new JSONObject(branchInformation);
            this.id = information.getString("id");
            this.fId = information.getString("fId");
            this.xsName = information.getString("xsName");
            this.name = information.getString("name");
            this.password = information.getString("password");
            this.dzName = information.getString("dzName");
            this.dzTel = information.getString("dzTel");
            this.dzPassword = information.getString("dzPassword");
            this.address = information.getString("address");
            this.tsTel = information.getString("tsTel");
            this.remark = information.getString("remark");
            this.kufang = information.getString("kufang");
            this.k_j_Area_Id = information.getString("k_j_Area_Id");
            this.danbaoren = information.getString("danbaoren");
            this.isSpecial = information.getString("isSpecial");
            this.branchType = information.getString("branchType");
            this.previewPwd = information.getString("previewPwd");
            this.stall = information.getString("stall");
            this.k_j_Storage_Id = information.getString("k_j_Storage_Id");
            this.ssLimit = information.getString("ssLimit");
            this.orderPower = information.getString("orderPower");
            this.branch_Stage = information.getString("branch_Stage");
            this.saleType = information.getString("saleType");
            this.province = information.getString("province");
            this.city = information.getString("city");
            this.county = information.getString("county");
            this.town = information.getString("town");
            this.oldName = information.getString("oldName");
            this.stallB = information.getString("stallB");
            this.integralTimes = information.getString("integralTimes");
    }
}
