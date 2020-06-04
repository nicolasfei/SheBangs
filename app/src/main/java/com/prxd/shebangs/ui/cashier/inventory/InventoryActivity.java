package com.prxd.shebangs.ui.cashier.inventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.prxd.shebangs.R;
import com.prxd.shebangs.branch.BranchKeeper;
import com.prxd.shebangs.common.DraftBill;
import com.prxd.shebangs.common.InputFormState;
import com.prxd.shebangs.common.OperateResult;
import com.prxd.shebangs.common.SheBangsDialog;
import com.prxd.shebangs.component.datetimepicker.DateFormatUtils;
import com.prxd.shebangs.hardware.scanner.PPdaScanner;
import com.prxd.shebangs.hardware.scanner.Scanner;
import com.prxd.shebangs.tool.Utils;
import com.prxd.shebangs.ui.BaseActivity;

import static com.prxd.shebangs.tool.Tool.dp2px;

public class InventoryActivity extends BaseActivity implements View.OnClickListener {

    private TextView cashier;
    private TextView businessTime;
    private TextView total;
    private EditText codeEdit;
    private Button draft;                   //草稿
    private Button submit;                  //批量提交

    private Scanner scanner;                //扫描头

    private InventoryGoodsAdapter adapter;
    private InventoryViewModel inventoryViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_inventory);

        inventoryViewModel = ViewModelProviders.of(this).get(InventoryViewModel.class);

        TextView shop = findViewById(R.id.shop);
        String shopName = getString(R.string.shop) + "\t\t\t\t\t\t" +
                "<font color=\"black\"><big>" + BranchKeeper.getInstance().information.name + "</big></font>";
        shop.setText(Html.fromHtml(shopName, Html.FROM_HTML_MODE_COMPACT));
        //营业员
        this.cashier = findViewById(R.id.cashier);
        updateCashier();
        this.cashier.setOnClickListener(this);
        //业务时间
        this.businessTime = findViewById(R.id.businessTime);
        updateBusinessTime(DateFormatUtils.long2Str(System.currentTimeMillis(), true));
        this.businessTime.setOnClickListener(this);

        //条码输入框
        this.codeEdit = findViewById(R.id.codeEdit);
        /**
         * 初始化扫描头
         */
        scanner = new PPdaScanner(this);
        scanner.setOnScannerScanResultListener(new Scanner.OnScannerScanResultListener() {
            @Override
            public void scanResult(String scan) {
                codeEdit.setText(scan);
                SheBangsDialog.showProgressDialog(InventoryActivity.this, getString(R.string.querying));
                queryCodeInventoryInfo(scan);
            }
        });
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                inventoryViewModel.codeDataChanged(codeEdit.getText().toString());
            }
        };
        this.codeEdit.addTextChangedListener(afterTextChangedListener);
        this.codeEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    SheBangsDialog.showProgressDialog(InventoryActivity.this, getString(R.string.querying));
                    queryCodeInventoryInfo(codeEdit.getText().toString());
                }
                return false;
            }
        });

        //盘点查询
        Button button = findViewById(R.id.button14);
        button.setOnClickListener(this);

        //商品展示list
        SwipeMenuListView swipeMenuListView = findViewById(R.id.swipeMenuListView);
        //左滑菜单
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
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
        swipeMenuListView.setMenuCreator(creator);
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        SheBangsDialog.showProgressDialog(InventoryActivity.this, getString(R.string.deleteing));
                        deleteInventoryGoods(position);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        this.adapter = new InventoryGoodsAdapter(this, inventoryViewModel.getGoodsInformationList());
        swipeMenuListView.setAdapter(this.adapter);

        //合计
        this.total = findViewById(R.id.total);
        //草稿
        this.draft = findViewById(R.id.draft);
        this.draft.setOnClickListener(this);
        //批量提交
        this.submit = findViewById(R.id.yes);
        this.submit.setOnClickListener(this);

        /**
         * 监听业务时间设置
         */
        inventoryViewModel.getSetBusinessTimeResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                if (operateResult.getSuccess() != null) {
                    updateBusinessTime(inventoryViewModel.getBusinessTime());
                }
                if (operateResult.getError() != null) {
                    Utils.toast(InventoryActivity.this, operateResult.getError().getErrorMsg());
                }
            }
        });

        /**
         * 监听条码输入框输入数据格式是否正确
         */
        inventoryViewModel.getInputFormState().observe(this, new Observer<InputFormState>() {
            @Override
            public void onChanged(InputFormState inputFormState) {
                if (inputFormState == null) {
                    return;
                }
                if (inputFormState.getInputError() != null) {
                    codeEdit.setError(inputFormState.getInputError());
                }
            }
        });
        /**
         * 监听删除商品信息
         */
        inventoryViewModel.getDeleteInventoryGoodsResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                SheBangsDialog.dismissProgressDialog();
                adapter.notifyDataSetChanged();
                updateTotal();
                setDraftSubmitButtonStatus();
            }
        });
        /**
         * 监听查询商品信息结果
         */
        inventoryViewModel.getQueryInventoryGoodsResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                SheBangsDialog.dismissProgressDialog();
                if (operateResult.getSuccess() != null) {
                    adapter.notifyDataSetChanged();
                    updateTotal();
                    setDraftSubmitButtonStatus();
                }
                if (operateResult.getError() != null) {
                    Utils.toast(InventoryActivity.this, operateResult.getError().getErrorMsg());
                }
            }
        });

        /**
         * 监听盘点单草稿操作结果
         */
        inventoryViewModel.getBusinessDraftResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                SheBangsDialog.dismissProgressDialog();
                if (operateResult.getSuccess() != null) {
                    showDraftSuccessDialog();
                }
            }
        });

        /**
         * 监听加载盘点草稿单结果
         */
        inventoryViewModel.getLoadDraftBillResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                if (operateResult.getSuccess() != null) {
                    SheBangsDialog.dismissProgressDialog();
                    loadDraftBillContentToActivity();
                }
                if (operateResult.getError() != null) {
                    Utils.toast(InventoryActivity.this, operateResult.getError().getErrorMsg());
                }
            }
        });

        /**
         * 监听批量提交结果
         */
        inventoryViewModel.getBatchSubmitResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                if (operateResult.getSuccess() != null) {
                    adapter.notifyDataSetChanged();
                    //更新时间
                    inventoryViewModel.setBusinessTime(DateFormatUtils.long2Str(System.currentTimeMillis(), true));
                    Utils.toast(InventoryActivity.this, getString(R.string.submit_success));
                }
                if (operateResult.getError() != null) {
                    Utils.toast(InventoryActivity.this, operateResult.getError().getErrorMsg());
                }
            }
        });

        //检查是否当前页面为加载草稿单
        Intent intent = getIntent();
        if (intent != null) {
            DraftBill draftBill = intent.getParcelableExtra("draft");
            if (draftBill != null) {
                SheBangsDialog.showProgressDialog(InventoryActivity.this, getString(R.string.loading));
                inventoryViewModel.loadDraftBill(draftBill);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         * 此方法用于初始化菜单，其中menu参数就是即将要显示的Menu实例。 返回true则显示该menu,false 则不显示;
         * (只会在第一次初始化菜单时调用) Inflate the menu; this adds items to the action bar
         * if it is present.
         */
        getMenuInflater().inflate(R.menu.darft_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.history:
                jumpToInventoryDraftActivity();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 跳转到草稿单历史Activity
     */
    private void jumpToInventoryDraftActivity() {

    }


    /**
     * 草稿单加载成功，显示内容到activity
     */
    private void loadDraftBillContentToActivity() {
        updateCashier();

        updateBusinessTime(inventoryViewModel.getBusinessTime());

        updateTotal();

        adapter.notifyDataSetChanged();
        setDraftSubmitButtonStatus();
    }

    /**
     * 显示保存草稿成功对话框
     */
    private void showDraftSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.draft_title))
                .setMessage(getString(R.string.draft_success))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inventoryActivityClear();
                    }
                }).setNegativeButton(R.string.cancel, null)
                .create().show();
    }

    /**
     * 盘点单子草稿成功，清空当前内容
     */
    private void inventoryActivityClear() {
        this.codeEdit.setText("");
        inventoryViewModel.contentClear();
    }

    /**
     * 根据当前盘点数据，设置草稿/提交按钮的状态
     */
    private void setDraftSubmitButtonStatus() {
        if (inventoryViewModel.getGoodsNum() == 0) {
            if (this.draft.isClickable()) {
                this.draft.setClickable(false);
                this.draft.setBackground(getDrawable(R.drawable.shape_rectangle_grey));
                this.submit.setClickable(false);
                this.submit.setBackground(getDrawable(R.drawable.shape_rectangle_grey));
            }
        } else {
            if (!this.draft.isClickable()) {
                this.draft.setClickable(true);
                this.draft.setBackground(getDrawable(R.drawable.shape_rectangle_red));
                this.submit.setClickable(true);
                this.submit.setBackground(getDrawable(R.drawable.shape_rectangle_red));
            }
        }
    }

    /**
     * 删除商品
     *
     * @param position position
     */
    private void deleteInventoryGoods(int position) {
        inventoryViewModel.deleteInventoryInfoForm(position);
    }

    /**
     * 查询盘点条码信息
     *
     * @param code 条码
     */
    private void queryCodeInventoryInfo(String code) {
        inventoryViewModel.queryInventoryInfoFormCode(code);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cashier:
                showSalespersonDialog();
                break;
            case R.id.businessTime:
                SheBangsDialog.showDateTimePickerDialog(InventoryActivity.this, new SheBangsDialog.OnDateTimePickListener() {
                    @Override
                    public void OnDateTimePick(String dateTime) {
                        inventoryViewModel.setBusinessTime(dateTime);
                    }
                });
                break;
            case R.id.button14:
                jump2InventoryQueryActivity();
                break;
            case R.id.draft:        //草稿
                inventoryDraft();
                break;
            case R.id.yes:          //批量提交
                batchSubmit();
                break;
        }
    }

    /**
     * 批量提交盘点数据
     */
    private void batchSubmit() {
        inventoryViewModel.batchSubmit();
    }

    /**
     * 当前盘点信息保存位草稿
     */
    private void inventoryDraft() {
        SheBangsDialog.showProgressDialog(InventoryActivity.this, getString(R.string.saveing));
        inventoryViewModel.businessDraft();
    }

    /**
     * 跳转到盘点查询activity
     */
    private void jump2InventoryQueryActivity() {
        Intent intent = new Intent(this, InventoryQueryActivity.class);
        startActivity(intent);
    }

    /**
     * 更新业务时间
     *
     * @param time
     */
    private void updateBusinessTime(String time) {
        String businessTimeValue = getString(R.string.business_time) + "\t\t\t\t\t\t" +
                "<font color=\"black\"><big>" + time + "</big></font>";
        businessTime.setText(Html.fromHtml(businessTimeValue, Html.FROM_HTML_MODE_COMPACT));
    }

    /**
     * 更新营业员
     */
    private void updateCashier() {
        String cashierNameValue = getString(R.string.cashier) + "\t\t\t\t\t\t" +
                "<font color=\"black\"><big>" + BranchKeeper.getInstance().onDutyGuide.guideName + "</big></font>";
        this.cashier.setText(Html.fromHtml(cashierNameValue, Html.FROM_HTML_MODE_COMPACT));
    }

    /**
     * 更新合计
     */
    private void updateTotal() {
        String totalValue = InventoryActivity.this.getString(R.string.total) +
                (inventoryViewModel.getGoodsNum() > 0 ? (":x" + inventoryViewModel.getGoodsNum() + "\t\t\t" +
                        getString(R.string.goods_price) + inventoryViewModel.getGoodsTotalPrice()) : "");
        total.setText(totalValue);
    }

    @Override
    public void onDutyGuideChange(String newGuideName) {
        updateCashier();
    }

    @Override
    protected void onDestroy() {
        this.scanner.scannerClose();
        super.onDestroy();
    }
}
