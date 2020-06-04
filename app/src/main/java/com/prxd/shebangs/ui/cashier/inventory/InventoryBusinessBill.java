package com.prxd.shebangs.ui.cashier.inventory;

import com.prxd.shebangs.common.BusinessBill;

import java.util.List;

public class InventoryBusinessBill extends BusinessBill {

    private List<InventoryGoodsInformation> inventoryGoodsList;
    private float goodsTotalPrice = 0;

    public InventoryBusinessBill(String billID, String businessTime, String billingPerson) {
        super(BusinessBill.INVENTORY_BILL, billID, businessTime, billingPerson);
    }

    public void setInventoryGoodsList(List<InventoryGoodsInformation> inventoryGoodsList) {
        this.inventoryGoodsList = inventoryGoodsList;
        for (InventoryGoodsInformation goods : this.inventoryGoodsList) {
            goodsTotalPrice += goods.salePrice;
        }
    }

    public float getGoodsTotalPrice() {
        return goodsTotalPrice;
    }

    public List<InventoryGoodsInformation> getInventoryGoodsList() {
        return inventoryGoodsList;
    }
}
