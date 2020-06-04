package com.prxd.shebangs.ui.cashier.cash;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.prxd.shebangs.R;
import com.prxd.shebangs.branch.BranchKeeper;
import com.prxd.shebangs.branch.Vip;
import com.prxd.shebangs.common.OperateResult;
import com.prxd.shebangs.tool.Utils;
import com.prxd.shebangs.ui.store.printer.PrinterActivity;

public class SettlementDetailedActivity extends AppCompatActivity {

    private SettlementDetailedViewModel detailedViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_settlement_detailed);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeButtonEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
        }

        detailedViewModel = ViewModelProviders.of(this).get(SettlementDetailedViewModel.class);
        detailedViewModel.setSaleData(getIntent().getBundleExtra("sale"));

        TextView storeName = findViewById(R.id.textView6);

        String storeNameText = getString(R.string.settlement_store_name) + "\t\t\t\t" +
                "<font color=\"black\"><big>" + BranchKeeper.getInstance().information.name + "</big></font>";
        storeName.setText(Html.fromHtml(storeNameText, Html.FROM_HTML_MODE_COMPACT));

        TextView cashierName = findViewById(R.id.textView7);
        String cashierNameS = getString(R.string.cashier_cash_cashier) + "\t\t\t\t" +
                "<font color=\"black\"><big>" + BranchKeeper.getInstance().onDutyGuide.guideName + "</big></font>";
        cashierName.setText(Html.fromHtml(cashierNameS, Html.FROM_HTML_MODE_COMPACT));

        TextView goodsTotal = findViewById(R.id.goods_total);
        String goodsTotalS = getString(R.string.settlement_goods_total) + detailedViewModel.goodsInformationList.size() + "\t" +
                "<font color=\"#FF5722\">" + getString(R.string.goods_price) + detailedViewModel.totalCast + "</font>";
        goodsTotal.setText(Html.fromHtml(goodsTotalS, Html.FROM_HTML_MODE_COMPACT));

        SwipeMenuListView saleList = findViewById(R.id.sale_list_view);
        SaleGoodsInformationAdapter goodsAdapter = new SaleGoodsInformationAdapter(this, detailedViewModel.goodsInformationList);
        saleList.setAdapter(goodsAdapter);

        Vip vip = detailedViewModel.vip;
        TextView vipName = findViewById(R.id.vip1);
        TextView buyIntegral = findViewById(R.id.vip2);
        TextView consumeIntegral = findViewById(R.id.vip3);
        TextView surplusIntegral = findViewById(R.id.vip4);
        if (vip != null) {
            String vipNameS = getString(R.string.settlement_vip_name) + "\t\t\t\t" +
                    "<font color=\"black\">" + vip.userName + "</font>";
            vipName.setText(Html.fromHtml(vipNameS, Html.FROM_HTML_MODE_COMPACT));

            String buyIntegralS = getString(R.string.settlement_buy_integral) + "\t\t\t\t" +
                    "<font color=\"black\">" + detailedViewModel.saleIntegral + "</font>";
            buyIntegral.setText(Html.fromHtml(buyIntegralS, Html.FROM_HTML_MODE_COMPACT));

            String consumeIntegralS = getString(R.string.settlement_consume_integral) + "\t\t\t\t" +
                    "<font color=\"black\">" + detailedViewModel.useIntegral + "</font>";
            consumeIntegral.setText(Html.fromHtml(consumeIntegralS, Html.FROM_HTML_MODE_COMPACT));

            String surplusIntegralS = getString(R.string.settlement_surplus_integral) + "\t\t\t\t" +
                    "<font color=\"black\">" + detailedViewModel.surplusIntegral + "</font>";
            surplusIntegral.setText(Html.fromHtml(surplusIntegralS, Html.FROM_HTML_MODE_COMPACT));
        } else {
            vipName.setText(getString(R.string.settlement_vip_name));
            buyIntegral.setText(getString(R.string.settlement_buy_integral));
            consumeIntegral.setText(getString(R.string.settlement_consume_integral));
            surplusIntegral.setText(getString(R.string.settlement_surplus_integral));
        }

        //收款金额
        TextView collection = findViewById(R.id.cash1);
        String collectionS = getString(R.string.settlement_collection) + "\t\t\t\t" +
                "<font color=\"black\">" + getString(R.string.goods_price) + detailedViewModel.collection + "</font>" + "\t\t\t\t" +
                //积分抵扣
                getString(R.string.settlement_deduction) + "\t\t" +
                "<font color=\"blue\">" + getString(R.string.goods_price) + detailedViewModel.deduction + "</font>";
        collection.setText(Html.fromHtml(collectionS, Html.FROM_HTML_MODE_COMPACT));

        //应收金额
        TextView receivable = findViewById(R.id.cash2);
        String receivableS = getString(R.string.settlement_receivable) + "\t\t\t\t" +
                "<font color=\"#FF5722\">" + getString(R.string.goods_price) + detailedViewModel.receivable + "</font>";
        receivable.setText(Html.fromHtml(receivableS, Html.FROM_HTML_MODE_COMPACT));

        //收款合计
        TextView total = findViewById(R.id.cash3);
        String totalS = getString(R.string.settlement_total) + "\t\t\t\t" +
                "<font color=\"black\">" + getString(R.string.goods_price) + detailedViewModel.collection + "</font>" + "\t\t\t\t" +
                //找零
                getString(R.string.settlement_text_change) + "\t\t\t" +
                "<font color=\"red\">" + getString(R.string.goods_price) + detailedViewModel.change + "</font>";
        total.setText(Html.fromHtml(totalS, Html.FROM_HTML_MODE_COMPACT));

        Button saleSubmit = findViewById(R.id.sale_submit);
        saleSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailedViewModel.printerSaleBill();
            }
        });
        //打印小票结果
        detailedViewModel.getPrintSaleBillResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                if (operateResult.getSuccess() != null) {
                    Utils.toast(SettlementDetailedActivity.this, getString(R.string.print_success));
                    showSuccessAndFinishDialog();
                }
                if (operateResult.getError() != null) {
                    Utils.toast(SettlementDetailedActivity.this, operateResult.getError().getErrorMsg());
                    //调整到蓝牙打印设置页面
                    Intent intent = new Intent(SettlementDetailedActivity.this, PrinterActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void showSuccessAndFinishDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.settlement_success_title))
                .setMessage(getString(R.string.settlement_success_finish))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SettlementDetailedActivity.this.setResult(1);
                        SettlementDetailedActivity.this.finish();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create().show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(0);
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
