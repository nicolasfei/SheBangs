package com.prxd.shebangs.ui.cashier.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.prxd.shebangs.R;
import com.prxd.shebangs.branch.BranchKeeper;
import com.prxd.shebangs.common.InputFormState;
import com.prxd.shebangs.common.OperateResult;
import com.prxd.shebangs.common.SheBangsDialog;
import com.prxd.shebangs.hardware.scanner.PPdaScanner;
import com.prxd.shebangs.hardware.scanner.Scanner;
import com.prxd.shebangs.tool.Utils;
import com.prxd.shebangs.ui.BaseActivity;

public class InventoryQueryActivity extends BaseActivity implements View.OnClickListener {

    private InventoryQueryViewModel inventoryQueryViewModel;

    private TextView cashier;               //收银员
    private TextView goodsClassText;        //商品类型
    private TextView supplierID;            //供应商ID
    private TextView codeStatus;            //条码状态
    private TextView isInventory;           //是否盘点
    private TextView oldGoodsId;            //旧货号
    private TextView newGoodsId;            //新货号
    private TextView warehousingTime;       //入库开始时间
    private TextView warehousingTimeTo;     //入库结束时间
    private EditText codeEdit;              //条码输入

    private Scanner scanner;                //扫描头

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_inventory_query);

        inventoryQueryViewModel = ViewModelProviders.of(this).get(InventoryQueryViewModel.class);
        //店铺
        TextView shop = findViewById(R.id.shop);
        String shopName = getString(R.string.shop) + "\t\t\t\t\t\t" +
                "<font color=\"black\"><big>" + BranchKeeper.getInstance().information.name + "</big></font>";
        shop.setText(Html.fromHtml(shopName, Html.FROM_HTML_MODE_COMPACT));
        //营业员
        this.cashier = findViewById(R.id.cashier);
        updateCashier();
        this.cashier.setOnClickListener(this);
        //商品类型
        this.goodsClassText = findViewById(R.id.goodsClass);
        this.goodsClassText.setOnClickListener(this);
        //供应商编号
        this.supplierID = findViewById(R.id.supplierID);
        this.supplierID.setOnClickListener(this);
        //条码状态
        this.codeStatus = findViewById(R.id.codeStatus);
        this.codeStatus.setOnClickListener(this);
        //是否盘点
        this.isInventory = findViewById(R.id.isInventory);
        this.isInventory.setOnClickListener(this);
        //旧货号
        this.oldGoodsId = findViewById(R.id.oldGoodsId);
        this.oldGoodsId.setOnClickListener(this);
        //新货号
        this.newGoodsId = findViewById(R.id.newGoodsId);
        this.newGoodsId.setOnClickListener(this);
        //入库时间
        this.warehousingTime = findViewById(R.id.warehousingTime);
        this.warehousingTime.setOnClickListener(this);
        //入库结束时间
        this.warehousingTimeTo = findViewById(R.id.warehousingTimeTo);
        this.warehousingTimeTo.setOnClickListener(this);
        //条码输入框
        this.codeEdit = findViewById(R.id.codeInput);
        /**
         * 初始化扫描头
         */
        scanner = new PPdaScanner(this);
        scanner.setOnScannerScanResultListener(new Scanner.OnScannerScanResultListener() {
            @Override
            public void scanResult(String scan) {
                codeEdit.setText(scan);
                inventoryQueryViewModel.setInventoryQueryCode(scan);
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
                inventoryQueryViewModel.codeDataChanged(codeEdit.getText().toString());
            }
        };
        this.codeEdit.addTextChangedListener(afterTextChangedListener);
        this.codeEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    inventoryQueryViewModel.setInventoryQueryCode(codeEdit.getText().toString());
                }
                return false;
            }
        });
        //清空按钮
        Button clear = findViewById(R.id.clear);
        clear.setOnClickListener(this);
        //查询按钮
        Button query = findViewById(R.id.query);
        query.setOnClickListener(this);

        /**
         * 监听商品类别更改
         */
        inventoryQueryViewModel.getSetGoodsClassResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                if (operateResult.getSuccess() != null) {
                    updateGoodsClass();
                }
            }
        });

        /**
         * 监听供应商ID更改
         */
        inventoryQueryViewModel.getSetSupplierIdResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                if (operateResult.getSuccess() != null) {
                    updateSupplierID();
                }
            }
        });

        /**
         * 监听条码状态
         */
        inventoryQueryViewModel.getSetCodeStatusResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                if (operateResult.getSuccess() != null) {
                    updateCodeStatus();
                }
            }
        });

        /**
         * 监听是否盘点
         */
        inventoryQueryViewModel.getSetIsInventoryResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                if (operateResult.getSuccess() != null) {
                    updateIsInventory();
                }
            }
        });

        /**
         * 监听旧货号
         */
        inventoryQueryViewModel.getSetOldGoodsIDResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                if (operateResult.getSuccess() != null) {
                    updateOldGoodsID();
                }
                if (operateResult.getError() != null) {
                    Utils.toast(InventoryQueryActivity.this, operateResult.getError().getErrorMsg());
                }
            }
        });

        /**
         * 监听新货号
         */
        inventoryQueryViewModel.getSetNewGoodsIDResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                if (operateResult.getSuccess() != null) {
                    updateNewGoodsID();
                }
                if (operateResult.getError() != null) {
                    Utils.toast(InventoryQueryActivity.this, operateResult.getError().getErrorMsg());
                }
            }
        });
        /**
         * 监听入库开始时间
         */
        inventoryQueryViewModel.getSetWarehousingStartTimeResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                if (operateResult.getSuccess() != null) {
                    updateWarehousingStartTime();
                }
            }
        });

        /**
         * 监听入口结束时间
         */
        inventoryQueryViewModel.getSetWarehousingEndTimeResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                if (operateResult.getSuccess() != null) {
                    updateWarehousingEndTime();
                }
            }
        });

        /**
         * 监听条码输入框输入数据格式是否正确
         */
        inventoryQueryViewModel.getInputFormState().observe(this, new Observer<InputFormState>() {
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
         * 监听条码输入
         */
        inventoryQueryViewModel.getSetInventoryQueryCodeResult().observe(this, new Observer<OperateResult>() {
            @Override
            public void onChanged(OperateResult operateResult) {
                if (operateResult.getSuccess() != null) {
                    updateInventoryQueryCode();
                }
                if (operateResult.getError() != null) {
                    Utils.toast(InventoryQueryActivity.this, operateResult.getError().getErrorMsg());
                }
            }
        });
    }

    @Override
    public void onDutyGuideChange(String newGuideName) {
        updateCashier();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cashier:
                super.showSalespersonDialog();
                break;
            case R.id.goodsClass:
                SheBangsDialog.showGoodsClassDialog(InventoryQueryActivity.this, BranchKeeper.getInstance().getGoodsClassesName(),
                        new SheBangsDialog.OnChoiceItemListener() {
                            @Override
                            public void onChoiceItem(String itemName) {
                                inventoryQueryViewModel.setGoodsClass(itemName);
                            }
                        });
                break;
            case R.id.supplierID:
                SheBangsDialog.showSupplierIdDialog(InventoryQueryActivity.this, BranchKeeper.getInstance().getSupplierIDs(),
                        new SheBangsDialog.OnChoiceItemListener() {
                            @Override
                            public void onChoiceItem(String itemName) {
                                inventoryQueryViewModel.setSupplierId(itemName);
                            }
                        });
                break;
            case R.id.codeStatus:
                SheBangsDialog.showCodeStatusDialog(InventoryQueryActivity.this, BranchKeeper.getInstance().getCodeStatus(),
                        new SheBangsDialog.OnChoiceItemListener() {
                            @Override
                            public void onChoiceItem(String itemName) {
                                inventoryQueryViewModel.setCodeStatus(itemName);
                            }
                        });
                break;
            case R.id.isInventory:
                SheBangsDialog.showIsInventoryDialog(InventoryQueryActivity.this, BranchKeeper.getInstance().getIsInventorySpin(),
                        new SheBangsDialog.OnChoiceItemListener() {
                            @Override
                            public void onChoiceItem(String itemName) {
                                inventoryQueryViewModel.setIsInventory(itemName);
                            }
                        });
                break;
            case R.id.oldGoodsId:
                SheBangsDialog.showOldGoodsIdDialog(InventoryQueryActivity.this,
                        new SheBangsDialog.OnInputFinishListener() {
                            @Override
                            public void onInputFinish(String itemName) {
                                inventoryQueryViewModel.setOldGoodsID(itemName);
                            }
                        });
                break;
            case R.id.newGoodsId:
                SheBangsDialog.showNewGoodsIdDialog(InventoryQueryActivity.this,
                        new SheBangsDialog.OnInputFinishListener() {
                            @Override
                            public void onInputFinish(String itemName) {
                                inventoryQueryViewModel.setNewGoodsID(itemName);
                            }
                        });
                break;
            case R.id.warehousingTime:
                SheBangsDialog.showDateTimePickerDialog(InventoryQueryActivity.this,
                        new SheBangsDialog.OnDateTimePickListener() {
                            @Override
                            public void OnDateTimePick(String dateTime) {
                                inventoryQueryViewModel.setWarehousingStartTime(dateTime);
                            }
                        });
                break;
            case R.id.warehousingTimeTo:
                SheBangsDialog.showDateTimePickerDialog(InventoryQueryActivity.this,
                        new SheBangsDialog.OnDateTimePickListener() {
                            @Override
                            public void OnDateTimePick(String dateTime) {
                                inventoryQueryViewModel.setWarehousingEndTime(dateTime);
                            }
                        });
                break;
            case R.id.clear:
                inventoryQueryViewModel.clearQueryCondition();
                break;
            case R.id.query:
                JumpToQueryInventoryResultActivity();
                break;
            default:
                break;
        }
    }

    /**
     * 跳转到查询并显示结果页面
     */
    private void JumpToQueryInventoryResultActivity() {
        Intent intent = new Intent(this, InventoryTakeActivity.class);
        intent.putExtra("queryCondition", inventoryQueryViewModel.getQueryInventoryResultCondition());
        startActivity(intent);
    }

    /**
     * 更新营业员
     */
    private void updateCashier() {
        String value = getString(R.string.cashier) + "\t\t\t\t\t\t" +
                "<font color=\"black\"><big>" + BranchKeeper.getInstance().onDutyGuide.guideName + "</big></font>";
        this.cashier.setText(Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT));
    }

    /**
     * 更新商品类型
     */
    private void updateGoodsClass() {
        String value = getString(R.string.goods_class) + "\t\t\t\t\t\t" +
                "<font color=\"black\"><big>" + inventoryQueryViewModel.getGoodsClass() + "</big></font>";
        this.goodsClassText.setText(Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT));
    }

    /**
     * 更新供应商ID
     */
    private void updateSupplierID() {
        String value = getString(R.string.supplier_id) + "\t\t\t\t\t\t" +
                "<font color=\"black\"><big>" + inventoryQueryViewModel.getSupplierID() + "</big></font>";
        this.supplierID.setText(Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT));
    }

    /**
     * 更新条码状态
     */
    private void updateCodeStatus() {
        String value = getString(R.string.code_status) + "\t\t\t\t\t\t" +
                "<font color=\"black\"><big>" + inventoryQueryViewModel.getCodeStatus() + "</big></font>";
        this.codeStatus.setText(Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT));
    }

    /**
     * 更新是否盘点
     */
    private void updateIsInventory() {
        String value = getString(R.string.inventory_is) + "\t\t\t\t\t\t" +
                "<font color=\"black\"><big>" + inventoryQueryViewModel.getIsInventory() + "</big></font>";
        this.isInventory.setText(Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT));
    }

    /**
     * 更新旧货号
     */
    private void updateOldGoodsID() {
        String value = getString(R.string.old_goods_id) + "\t\t\t\t\t\t" +
                "<font color=\"black\"><big>" + inventoryQueryViewModel.getOldGoodsID() + "</big></font>";
        this.oldGoodsId.setText(Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT));
    }

    /**
     * 更新新货号
     */
    private void updateNewGoodsID() {
        String value = getString(R.string.new_goods_id) + "\t\t\t\t\t\t" +
                "<font color=\"black\"><big>" + inventoryQueryViewModel.getNewGoodsID() + "</big></font>";
        this.newGoodsId.setText(Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT));
    }

    /**
     * 更新入库开始时间
     */
    private void updateWarehousingStartTime() {
        String value = getString(R.string.warehousing_time) + "\t\t\t\t\t\t" +
                "<font color=\"black\"><big>" + inventoryQueryViewModel.getWarehousingStartTime() + "</big></font>";
        this.warehousingTime.setText(Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT));
    }

    /**
     * 更新入库结束时间
     */
    private void updateWarehousingEndTime() {
        String value = getString(R.string.to) + "\t\t\t\t\t\t" +
                "<font color=\"black\"><big>" + inventoryQueryViewModel.getWarehousingEndTime() + "</big></font>";
        this.warehousingTimeTo.setText(Html.fromHtml(value, Html.FROM_HTML_MODE_COMPACT));
    }

    /**
     * 更新查询条码
     */
    private void updateInventoryQueryCode() {
        codeEdit.setText(inventoryQueryViewModel.getInventoryQueryCode());
    }

    @Override
    protected void onDestroy() {
        this.scanner.scannerClose();
        super.onDestroy();
    }
}
