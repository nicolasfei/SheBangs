package com.prxd.shebangs.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.prxd.shebangs.R;
import com.prxd.shebangs.branch.BranchKeeper;
import com.prxd.shebangs.branch.Guide;

import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeButtonEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 收银员选择
     */
    public void showSalespersonDialog() {
        int checkedSalesperson = 0;
        List<Guide> guides = BranchKeeper.getInstance().guides;
        String onDutyName = BranchKeeper.getInstance().onDutyGuide.guideName;
        final String[] salesperson = new String[guides.size()];
        for (int i = 0; i < guides.size(); i++) {
            salesperson[i] = guides.get(i).guideName;
            if (salesperson[i].equals(onDutyName)) {
                checkedSalesperson = i;
            }
        }
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.login_guide_choice))
                .setSingleChoiceItems(salesperson, checkedSalesperson, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BranchKeeper.getInstance().setOnDutyGuide(salesperson[which]);
                        onDutyGuideChange(salesperson[which]);
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    /**
     * 营业员更换
     *
     * @param newGuideName 新营业员名字
     */
    public abstract void onDutyGuideChange(String newGuideName);
}
