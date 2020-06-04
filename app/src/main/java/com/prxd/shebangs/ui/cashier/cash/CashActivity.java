package com.prxd.shebangs.ui.cashier.cash;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.prxd.shebangs.R;
import com.prxd.shebangs.app.SheBangsApp;
import com.prxd.shebangs.branch.BranchKeeper;
import com.prxd.shebangs.common.OperateResult;
import com.prxd.shebangs.common.SheBangsDialog;
import com.prxd.shebangs.tool.Utils;
import com.prxd.shebangs.ui.BaseActivity;
import com.prxd.shebangs.hardware.scanner.PPdaScanner;
import com.prxd.shebangs.hardware.scanner.Scanner;

import java.util.List;

import static com.prxd.shebangs.tool.Tool.dp2px;

public class CashActivity extends BaseActivity {

    private static final String TAG = "CashActivity";
    private TextView saleNum, saleTotalPrice;

    private SaleGoodsInformationAdapter goodsAdapter;
    private EditText codeInput;             //条码输入
    private Scanner scanner;                //扫描头
    private Button settlement;              //结算
    private CashViewModel cashViewModel;
    private TextView salespersonTextView;   //收银员
    private TextView goodsCount;            //商品数量
    private TextView priceCount;            //总价合计

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_cash);
        cashViewModel = ViewModelProviders.of(this).get(CashViewModel.class);

        //EditText
        codeInput = findViewById(R.id.editText);
        goodsCount = findViewById(R.id.textView);
        priceCount = findViewById(R.id.textView2);

        /**
         * 初始化扫描头
         */
        scanner = new PPdaScanner(this);
        scanner.setOnScannerScanResultListener(new Scanner.OnScannerScanResultListener() {
            @Override
            public void scanResult(String scan) {
                codeInput.setText(scan);
                queryCodeSaleInfo(scan);
            }
        });


        final Button oldSale = findViewById(R.id.button10);
        oldSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //旧货销售
                oldGoodsSale();
            }
        });

        final SwipeMenuListView saleList = findViewById(R.id.sale_list_view);
        settlement = findViewById(R.id.button11);
        settlement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //结算
                saleSettlement();
            }
        });

        saleNum = findViewById(R.id.textView);
        saleTotalPrice = findViewById(R.id.textView2);
        String testJson = "{\"id\": \"2003217EAE2B\",\"k_j_GoodsClass_Id\": \"170708-8D26BC15\",\"goodsClassName\": \"衣服\",\"isDaiMai\": \"正常\", \"num\": 1,\"salePrice\": 45, \"totalPrice\": 45,\"stockTotal\": 9}";
        String testJson1 = "{\"id\": \"2003217EAE2C\",\"k_j_GoodsClass_Id\": \"170708-8D26BC15\",\"goodsClassName\": \"衣服\",\"isDaiMai\": \"正常\", \"num\": 1,\"salePrice\": 45, \"totalPrice\": 45,\"stockTotal\": 9}";
        String testJson2 = "{\"id\": \"2003217EAE2D\",\"k_j_GoodsClass_Id\": \"170708-8D26BC15\",\"goodsClassName\": \"衣服\",\"isDaiMai\": \"正常\", \"num\": 1,\"salePrice\": 45, \"totalPrice\": 45,\"stockTotal\": 9}";
        String testJson3 = "{\"id\": \"2003217EAE2E\",\"k_j_GoodsClass_Id\": \"170708-8D26BC15\",\"goodsClassName\": \"衣服\",\"isDaiMai\": \"正常\", \"num\": 1,\"salePrice\": 45, \"totalPrice\": 45,\"stockTotal\": 9}";

        List<GoodsInformation> saleMsg = cashViewModel.getGoodsInformationList();
//        saleMsg.add(new GoodsInformation(testJson));
//        saleMsg.add(new GoodsInformation(testJson1));
//        saleMsg.add(new GoodsInformation(testJson2));
//        saleMsg.add(new GoodsInformation(testJson3));
        goodsAdapter = new SaleGoodsInformationAdapter(this, saleMsg);
        saleList.setAdapter(goodsAdapter);
        //左滑菜单
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "look" item
                SwipeMenuItem detailsItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                detailsItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                detailsItem.setWidth(dp2px(90));
                // set item title
                detailsItem.setTitle(getString(R.string.details));
                // set item title fontsize
                detailsItem.setTitleSize(18);
                // set item title font color
                detailsItem.setTitleColor(Color.WHITE);
                // add to menu
//                menu.addMenuItem(detailsItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set item title
                deleteItem.setTitle(getString(R.string.delete));
                // set item title fontsize
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        //添加左滑菜单
        saleList.setMenuCreator(creator);
        saleList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Log.d(TAG, "onMenuItemClick: position is " + position);
                switch (index) {
                    case 0:
                        Log.d(TAG, "onMenuItemClick: click showGoodsDetails");
//                        showGoodsDetails(position);
                        deleteGoods(position);
                        break;
                    case 1:
                        Log.d(TAG, "onMenuItemClick: click deleteGoods!");
                        deleteGoods(position);
                        break;
                }
                return true;
            }
        });

        //初始化销售员
        salespersonTextView = findViewById(R.id.salesperson);
        salespersonTextView.setText((getString(R.string.cashier_cash_cashier) + "：" + BranchKeeper.getInstance().onDutyGuide.guideName));
        salespersonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSalespersonDialog();
            }
        });


        //设置查询服务器商品数据监听
        cashViewModel.getGoodsOperateResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult OperateResult) {        //销售商品查询结果
                SheBangsDialog.dismissProgressDialog();
                if (OperateResult.getSuccess() != null) {
                    goodsAdapter.notifyDataSetChanged();
                    checkSettlement();
                }
                if (OperateResult.getError() != null) {
                    Utils.toast(CashActivity.this, OperateResult.getError().getErrorMsg());
                }
            }
        });

        //设置添加旧货监听
        cashViewModel.getOldGoodsOperateResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult OperateResult) {
                SheBangsDialog.dismissProgressDialog();
                if (OperateResult.getSuccess() != null) {
                    goodsAdapter.notifyDataSetChanged();
                    checkSettlement();
                }
                if (OperateResult.getError() != null) {
                    Utils.toast(CashActivity.this, OperateResult.getError().getErrorMsg());
                }
            }
        });

        //添加删除商品监听
        cashViewModel.getRemoveGoodsResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult OperateResult) {
                if (OperateResult.getSuccess() != null) {
                    goodsAdapter.notifyDataSetChanged();
                    checkSettlement();
                }
                if (OperateResult.getError() != null) {
                    Utils.toast(CashActivity.this, OperateResult.getError().getErrorMsg());
                }
            }
        });

        //设置结算按钮状态
        checkSettlement();
    }

    @Override
    public void onDutyGuideChange(String newGuideName) {
        salespersonTextView.setText((this.getString(R.string.cashier_cash_cashier) + "：" + newGuideName));
    }

    /**
     * 检测结算按钮
     */
    private void checkSettlement() {
        if (cashViewModel.getGoodsCount() == 0) {
            if (settlement.isClickable()) {
                settlement.setBackground(getDrawable(R.drawable.shape_rectangle_grey));
                settlement.setClickable(false);
            }
        } else {
            if (!settlement.isClickable()) {
                settlement.setBackground(getDrawable(R.drawable.shape_rectangle_red));
                settlement.setClickable(true);
            }
        }
        goodsCount.setText((getString(R.string.cashier_goods_num) + cashViewModel.getGoodsCount()));
        priceCount.setText((getString(R.string.cashier_goods_price_total) + getString(R.string.goods_price) + cashViewModel.getPriceTotal()));
    }

    /**
     * 查询销售商品信息
     *
     * @param goodsCode 商品条码
     */
    private void queryCodeSaleInfo(String goodsCode) {
        SheBangsDialog.showProgressDialog(this, getString(R.string.query_goods));
        cashViewModel.queryGoodsInformation(goodsCode);
    }

    /**
     * 查询旧货商品信息
     *
     * @param oldGoods 旧货
     */
    private void queryOldGoodsInfo(GoodsInformation oldGoods) {
        SheBangsDialog.showProgressDialog(this, getString(R.string.query_goods));
        cashViewModel.addOldGoodsSale(oldGoods);
    }

    /**
     * 删除商品
     *
     * @param position position
     */
    private void deleteGoods(int position) {
        cashViewModel.removeGoods(position);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scanner.scannerOpen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanner.scannerSuspend();
    }

    /**
     * 旧货销售
     */
    private void oldGoodsSale() {
        final CashOldSaleDialog dialog = new CashOldSaleDialog(this);
        dialog.setTitle(R.string.cashier_old_sale);
        dialog.setOnOldGoodsSaleDialogListener(new CashOldSaleDialog.OnOldGoodsSaleDialogListener() {
            @Override
            public void saleOldGoods(String goodsType, int num, float price) {
                GoodsInformation oldGoods = new GoodsInformation();
                oldGoods.num = num;
                oldGoods.salePrice = price;
                oldGoods.totalPrice = price * num;
                oldGoods.goodsClassName = goodsType;
                oldGoods.id = getString(R.string.cashier_old_id);
                dialog.dismiss();
                queryOldGoodsInfo(oldGoods);        //添加旧货
            }
        });
        dialog.show();
    }

    /**
     * 结算
     */
    private void saleSettlement() {
        Intent intent = new Intent(this, SettlementActivity.class);
        intent.putParcelableArrayListExtra("groupMsgs", cashViewModel.getGoodsInformationList());
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 2) {
            return;
        }
        switch (resultCode) {
            case 0:
                break;
            case 1:     //收银完成，开始新的收银
                codeInput.setText("");
                cashViewModel.startNewCashier();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        scanner.scannerClose();
        super.onDestroy();
    }
}
