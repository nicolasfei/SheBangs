package com.prxd.shebangs.ui.store;

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
import com.prxd.shebangs.ui.store.printer.PrinterActivity;

import java.util.ArrayList;
import java.util.List;

public class StoreViewModel extends ViewModel {

    private MutableLiveData<OperateResult> updateNavNumResult;
    private List<ModuleNavigation> content = new ArrayList<>();

    public StoreViewModel() {
        this.updateNavNumResult = new MutableLiveData<>();

        content.add(new ModuleNavigation(true, SheBangsApp.getInstance().getString(R.string.nav_myStore_device_manager), 0, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_myStore_printer), R.mipmap.ic_launcher, PrinterActivity.class));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_myStore_scanner), R.mipmap.ic_launcher, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_myStore_setting), R.mipmap.ic_launcher, null));
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