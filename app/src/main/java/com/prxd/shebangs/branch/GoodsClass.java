package com.prxd.shebangs.branch;

import org.json.JSONException;
import org.json.JSONObject;

public class GoodsClass {
    private String id;
    private String name;

    public GoodsClass(String id, String name){
        this.id=id;
        this.name=name;
    }

    public GoodsClass(String data) throws JSONException {
        JSONObject jsonObject=new JSONObject(data);
        this.id = jsonObject.getString("id");
        this.name=jsonObject.getString("name");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
