package com.prxd.shebangs.ui.cashier;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prxd.shebangs.R;
import com.prxd.shebangs.app.SheBangsApp;
import com.prxd.shebangs.common.ModuleNavigation;
import com.prxd.shebangs.common.OperateInUserView;
import com.prxd.shebangs.common.OperateResult;
import com.prxd.shebangs.ui.cashier.cash.CashActivity;
import com.prxd.shebangs.ui.cashier.inventory.InventoryActivity;
import com.prxd.shebangs.ui.cashier.inventory.InventoryQueryActivity;

import java.util.ArrayList;
import java.util.List;

public class CashierViewModel extends ViewModel {

    private MutableLiveData<OperateResult> updateNavNumResult;
    private List<ModuleNavigation> content = new ArrayList<>();

    public CashierViewModel() {
        this.updateNavNumResult = new MutableLiveData<>();
        //content.add(new ModuleNavigation(true, getString(R.string.nav_cashier_title), 0, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_cashier_cashier), R.drawable.ic_cashier, CashActivity.class));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_cashier_inventory), R.drawable.ic_inventory, InventoryActivity.class));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_cashier_inventory_query), R.drawable.ic_inventory_query, InventoryQueryActivity.class));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_cashier_receiver), R.drawable.ic_receiver, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_cashier_transfer_in), R.drawable.ic_transfer, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_cashier_transfer_out), R.drawable.ic_transfer, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_cashier_bill_query), R.drawable.ic_bill_query, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_cashier_vip_query), R.drawable.ic_vip_query, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_cashier_sales_query), R.drawable.ic_sales, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_cashier_sales_statistics), R.drawable.ic_sale_statistics, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_cashier_remittance), R.drawable.ic_remittance, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_cashier_order_submit), R.drawable.ic_order, null));
    }

    public LiveData<OperateResult> getUpdateNavNumResult() {
        return updateNavNumResult;
    }

    /**
     * 更新ModuleNavigation通知数字
     *
     * @param position position
     * @param num      num
     */
    public void updateModuleNoticeNum(int position, int num) {
        ModuleNavigation item = content.get(position);
        if (item.getNavigationNum() != num) {
            item.setNavigationNum(num);
            updateNavNumResult.setValue(new OperateResult(new OperateInUserView(null)));
        }
    }

    public ModuleNavigation getModuleNavigation(int position) {
        return content.get(position);
    }

    public List<ModuleNavigation> getModuleNavigationList() {
        return content;
    }
}