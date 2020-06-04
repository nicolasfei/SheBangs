package com.prxd.shebangs.branch;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 店铺导购
 */
public class Guide {

    public String guideId;
    public String guideName;

    public Guide(String guideId, String guideName) {
        this.guideId = guideId;
        this.guideName = guideName;
    }

    public Guide(String data) throws JSONException {
            JSONObject jsonObject = new JSONObject(data);
            this.guideId = jsonObject.getString("guideId");
            this.guideName = jsonObject.getString("guideName");
    }
}
