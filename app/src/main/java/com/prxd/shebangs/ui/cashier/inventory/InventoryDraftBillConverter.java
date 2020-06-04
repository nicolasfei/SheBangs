package com.prxd.shebangs.ui.cashier.inventory;

import com.prxd.shebangs.R;
import com.prxd.shebangs.app.SheBangsApp;
import com.prxd.shebangs.branch.BranchKeeper;
import com.prxd.shebangs.common.BusinessBill;
import com.prxd.shebangs.common.BusinessDraftBillConverter;
import com.prxd.shebangs.common.DraftBill;
import com.prxd.shebangs.serverInterface.CommandResponse;
import com.prxd.shebangs.serverInterface.CommandTypeEnum;
import com.prxd.shebangs.serverInterface.CommandVo;
import com.prxd.shebangs.serverInterface.HttpHandler;
import com.prxd.shebangs.serverInterface.Invoker;
import com.prxd.shebangs.serverInterface.cash.CashInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryDraftBillConverter extends BusinessDraftBillConverter {
    @Override
    public DraftBill convertBusinessToDraft(BusinessBill business) {
        if (!(business instanceof InventoryBusinessBill)) {
            return null;
        }
        try {
            InventoryBusinessBill bill = (InventoryBusinessBill) business;
            JSONObject object = new JSONObject();
            object.putOpt("time", bill.businessTime);
            object.putOpt("person", bill.billingPerson);
            object.putOpt("price", bill.getGoodsTotalPrice());
            JSONArray array = new JSONArray();
            for (InventoryGoodsInformation goods : bill.getInventoryGoodsList()) {
                array.put(goods.k_j_BarCode_Id);
            }
            object.putOpt("data", array);
            String billContent = object.toString();
            return new DraftBill(BusinessBill.INVENTORY_BILL, bill.billID, billContent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void convertBusinessFormDraft(DraftBill draft) {
        if (!draft.billType.equals(BusinessBill.INVENTORY_BILL)) {
            if (super.listener != null) {
                super.listener.convertError(SheBangsApp.getInstance().getString(R.string.bill_type_mismatch));
            }
            return;
        }

        try {
            JSONObject json = new JSONObject(draft.content);
            InventoryBusinessBill businessBill = new InventoryBusinessBill(draft.billID, json.getString("time"), json.getString("person"));
            JSONArray contents = json.getJSONArray("data");
            final List<InventoryGoodsInformation> inventoryGoodsList = new ArrayList<>();
            for (int i = 0; i < contents.length(); i++) {
                final String code = contents.getString(i);
                //根据条码查询最新的数据
                CommandVo vo = new CommandVo();
                vo.typeEnum = CommandTypeEnum.COMMAND_BRANCH_CASH;
                vo.url = CashInterface.GetBarCodePanDianInfo;
                vo.contentType = HttpHandler.ContentType_APP;
                vo.requestMode = HttpHandler.RequestMode_GET;
                Map<String, String> parameters = new HashMap<>();
                parameters.put("code", code);
                parameters.put("userkey", BranchKeeper.getInstance().userKey);
                vo.parameters = parameters;
                Invoker.getInstance().setOnEchoResultCallback(new Invoker.OnExecResultCallback() {
                    @Override
                    public void execResult(CommandResponse result) {
                        if (!result.success) {
                            if (InventoryDraftBillConverter.super.listener != null) {
                                InventoryDraftBillConverter.super.listener.convertError(SheBangsApp.getInstance().getString(R.string.bill_query_error) + ":" + code +
                                        "," + SheBangsApp.getInstance().getString(R.string.check_network));
                            }
                        } else {
                            try {
                                inventoryGoodsList.add(new InventoryGoodsInformation(result.data));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                Invoker.getInstance().exec(vo);
            }
            businessBill.setInventoryGoodsList(inventoryGoodsList);
            if (super.listener != null) {
                super.listener.convertFinish(businessBill);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (super.listener != null) {
                super.listener.convertError(SheBangsApp.getInstance().getString(R.string.bill_format_error));
            }
        }
    }
}
