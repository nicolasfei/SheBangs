package com.prxd.shebangs.ui.server;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prxd.shebangs.R;
import com.prxd.shebangs.app.SheBangsApp;
import com.prxd.shebangs.common.ModuleNavigation;
import com.prxd.shebangs.common.OperateInUserView;
import com.prxd.shebangs.common.OperateResult;

import java.util.ArrayList;
import java.util.List;

public class ServerViewModel extends ViewModel {

    private MutableLiveData<OperateResult> updateNavNumResult;
    private List<ModuleNavigation> content = new ArrayList<>();

    public ServerViewModel() {
        this.updateNavNumResult = new MutableLiveData<>();

        //<!--店铺管理-->
        content.add(new ModuleNavigation(true, SheBangsApp.getInstance().getString(R.string.nav_back_store_manager), 0, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_back_store_staff_manager), R.mipmap.test2, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_back_store_inventory_classification), R.mipmap.test1, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_back_store_inventory_details), R.mipmap.test2, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_back_store_returns), R.mipmap.test1, null));
        //<!--财务申报-->
        content.add(new ModuleNavigation(true, SheBangsApp.getInstance().getString(R.string.nav_back_financial_declaration), 0, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_back_financial_rent_registration), R.mipmap.test2, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_back_financial_wages_registration), R.mipmap.test1, null));
        //<!--会员管理-->
        content.add(new ModuleNavigation(true, SheBangsApp.getInstance().getString(R.string.nav_back_vip_manager), 0, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_back_vip_user), R.mipmap.test2, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_back_vip_integral_record), R.mipmap.test1, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_back_vip_integral_consume_record), R.mipmap.test2, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_back_vip_integral_detailed), R.mipmap.test1, null));
        //<!--订货管理-->
        content.add(new ModuleNavigation(true, SheBangsApp.getInstance().getString(R.string.nav_back_book_manager), 0, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_back_book_overbook), R.mipmap.test2, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_back_book_order), R.mipmap.test1, null));
        //<!--查询统计-->
        content.add(new ModuleNavigation(true, SheBangsApp.getInstance().getString(R.string.nav_back_query_statistics), 0, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_back_query_management), R.mipmap.test2, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_back_query_sale_ranking), R.mipmap.test1, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_back_query_sale_statistics), R.mipmap.test2, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_back_query_best_seller_single), R.mipmap.test1, null));
        content.add(new ModuleNavigation(false, SheBangsApp.getInstance().getString(R.string.nav_back_query_best_seller_class), R.mipmap.test1, null));
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
