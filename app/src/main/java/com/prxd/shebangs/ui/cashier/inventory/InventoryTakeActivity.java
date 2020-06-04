package com.prxd.shebangs.ui.cashier.inventory;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProviders;

import com.prxd.shebangs.R;
import com.prxd.shebangs.ui.BaseActivity;

public class InventoryTakeActivity extends BaseActivity {

    InventoryTakeViewModel inventoryViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_inventory_take);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeButtonEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
        }

        inventoryViewModel = ViewModelProviders.of(this).get(InventoryTakeViewModel.class);
    }

    @Override
    public void onDutyGuideChange(String newGuideName) {

    }
}
