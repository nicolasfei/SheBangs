package com.prxd.shebangs.branch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PayCase {
    private String[] payment;

    public PayCase(String data) throws JSONException {
        JSONArray array = new JSONArray(data);
        payment = new String[array.length()];
        for (int i = 0; i < array.length(); i++) {
            JSONObject pay = array.getJSONObject(i);
            payment[i] = pay.getString("payment");
        }
    }

    public void setPayment(String[] payment) {
        this.payment = payment;
    }

    public String[] getPayment() {
        return payment;
    }
}
