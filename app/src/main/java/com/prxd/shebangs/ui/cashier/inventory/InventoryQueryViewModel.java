package com.prxd.shebangs.ui.cashier.inventory;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prxd.shebangs.R;
import com.prxd.shebangs.app.SheBangsApp;
import com.prxd.shebangs.common.InputFormState;
import com.prxd.shebangs.common.OperateError;
import com.prxd.shebangs.common.OperateInUserView;
import com.prxd.shebangs.common.OperateResult;
import com.prxd.shebangs.tool.Tool;

import java.util.HashMap;

public class InventoryQueryViewModel extends ViewModel {

    private String goodsClass;
    private String supplierID;
    private String codeStatus;
    private String isInventory;
    private String oldGoodsID;
    private String newGoodsID;
    private String warehousingStartTime;
    private String warehousingEndTime;
    private String inventoryQueryCode;

    private MutableLiveData<OperateResult> setGoodsClassResult;     //设置商品类型结果
    private MutableLiveData<OperateResult> setSupplierIdResult;     //设置供应商ID结果
    private MutableLiveData<OperateResult> setCodeStatusResult;     //设置条码状态结果
    private MutableLiveData<OperateResult> setIsInventoryResult;    //设置是否盘点结果
    private MutableLiveData<OperateResult> setOldGoodsIDResult;     //设置旧货号ID结果
    private MutableLiveData<OperateResult> setNewGoodsIDResult;     //设置新货号ID结果
    private MutableLiveData<OperateResult> setWarehousingStartTimeResult;   //设置入库开始时间结果
    private MutableLiveData<OperateResult> setWarehousingEndTimeResult;     //设置入库结算时间结果
    private MutableLiveData<InputFormState> inputFormState;                 //输入格式是否正确
    private MutableLiveData<OperateResult> setInventoryQueryCodeResult;     //输入条码结果

    public InventoryQueryViewModel() {
        this.setGoodsClassResult = new MutableLiveData<>();
        this.setSupplierIdResult = new MutableLiveData<>();
        this.setCodeStatusResult = new MutableLiveData<>();
        this.setIsInventoryResult = new MutableLiveData<>();
        this.setOldGoodsIDResult = new MutableLiveData<>();
        this.setNewGoodsIDResult = new MutableLiveData<>();
        this.setWarehousingStartTimeResult = new MutableLiveData<>();
        this.setWarehousingEndTimeResult = new MutableLiveData<>();
        this.inputFormState = new MutableLiveData<>();
        this.setInventoryQueryCodeResult = new MutableLiveData<>();
    }

    public LiveData<OperateResult> getSetGoodsClassResult() {
        return setGoodsClassResult;
    }

    public LiveData<OperateResult> getSetSupplierIdResult() {
        return setSupplierIdResult;
    }

    public LiveData<OperateResult> getSetCodeStatusResult() {
        return setCodeStatusResult;
    }

    public LiveData<OperateResult> getSetIsInventoryResult() {
        return setIsInventoryResult;
    }

    public LiveData<OperateResult> getSetOldGoodsIDResult() {
        return setOldGoodsIDResult;
    }

    public LiveData<OperateResult> getSetNewGoodsIDResult() {
        return setNewGoodsIDResult;
    }

    public LiveData<OperateResult> getSetWarehousingEndTimeResult() {
        return setWarehousingEndTimeResult;
    }

    public LiveData<OperateResult> getSetWarehousingStartTimeResult() {
        return setWarehousingStartTimeResult;
    }

    public LiveData<InputFormState> getInputFormState() {
        return inputFormState;
    }

    public LiveData<OperateResult> getSetInventoryQueryCodeResult() {
        return setInventoryQueryCodeResult;
    }

    public void setGoodsClass(String goodsClass) {
        this.goodsClass = goodsClass;
        this.setGoodsClassResult.setValue(new OperateResult(new OperateInUserView(null)));
    }

    public String getGoodsClass() {
        return goodsClass;
    }

    public void setSupplierId(String supplierID) {
        this.supplierID = supplierID;
        this.setSupplierIdResult.setValue(new OperateResult(new OperateInUserView(null)));
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setCodeStatus(String codeStatus) {
        this.codeStatus = codeStatus;
        this.setCodeStatusResult.setValue(new OperateResult(new OperateInUserView(null)));
    }

    public String getCodeStatus() {
        return codeStatus;
    }

    public void setIsInventory(String isInventory) {
        this.isInventory = isInventory;
        this.setIsInventoryResult.setValue(new OperateResult(new OperateInUserView(null)));
    }

    public String getIsInventory() {
        return isInventory;
    }

    public void setOldGoodsID(String oldGoodsID) {
        if (Tool.isGoodsCodeValid(oldGoodsID)) {
            this.oldGoodsID = oldGoodsID;
            this.setOldGoodsIDResult.setValue(new OperateResult(new OperateInUserView(null)));
        } else {
            this.setOldGoodsIDResult.setValue(new OperateResult(new OperateError(0, SheBangsApp.getInstance().getString(R.string.goods_id_valid), null)));
        }
    }

    public String getOldGoodsID() {
        return oldGoodsID;
    }

    public void setNewGoodsID(String newGoodsID) {
        if (Tool.isGoodsCodeValid(newGoodsID)) {
            this.newGoodsID = newGoodsID;
            this.setNewGoodsIDResult.setValue(new OperateResult(new OperateInUserView(null)));
        } else {
            this.setNewGoodsIDResult.setValue(new OperateResult(new OperateError(0, SheBangsApp.getInstance().getString(R.string.goods_id_valid), null)));
        }
    }

    public String getNewGoodsID() {
        return newGoodsID;
    }

    public void setWarehousingStartTime(String dateTime) {
        this.warehousingStartTime = dateTime;
        this.setWarehousingStartTimeResult.setValue(new OperateResult(new OperateInUserView(null)));
    }

    public String getWarehousingStartTime() {
        return warehousingStartTime;
    }

    public void setWarehousingEndTime(String dateTime) {
        this.warehousingEndTime = dateTime;
        this.setWarehousingEndTimeResult.setValue(new OperateResult(new OperateInUserView(null)));
    }

    public String getWarehousingEndTime() {
        return warehousingEndTime;
    }

    /**
     * 条码输出框发生变化，检查是否格式有错误
     *
     * @param code 条码
     */
    public void codeDataChanged(String code) {
        if (Tool.isCodeValid(code)) {
            inputFormState.setValue(new InputFormState(true));
        } else {
            inputFormState.setValue(new InputFormState(SheBangsApp.getInstance().getString(R.string.code_format_error)));
        }
    }

    public void setInventoryQueryCode(String code) {
        this.inventoryQueryCode = code;
        this.setInventoryQueryCodeResult.setValue(new OperateResult(new OperateInUserView(null)));
    }

    public String getInventoryQueryCode() {
        return inventoryQueryCode;
    }

    /**
     * 获取查询盘点结果条件
     */
    public HashMap<String, String> getQueryInventoryResultCondition() {
        HashMap<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(this.goodsClass)) {
            map.put("goodsClass", this.goodsClass);
        }
        if (!TextUtils.isEmpty(this.supplierID)) {
            map.put("supplierID", this.supplierID);
        }
        if (!TextUtils.isEmpty(this.codeStatus)) {
            map.put("codeStatus", this.codeStatus);
        }
        if (!TextUtils.isEmpty(this.isInventory)) {
            map.put("isInventory", this.isInventory);
        }
        if (!TextUtils.isEmpty(this.oldGoodsID)) {
            map.put("oldGoodsID", this.oldGoodsID);
        }
        if (!TextUtils.isEmpty(this.newGoodsID)) {
            map.put("newGoodsID", this.newGoodsID);
        }
        if (!TextUtils.isEmpty(this.warehousingStartTime)) {
            map.put("warehousingStartTime", this.warehousingStartTime);
        }
        if (!TextUtils.isEmpty(this.warehousingEndTime)) {
            map.put("warehousingEndTime", this.warehousingEndTime);
        }
        if (!TextUtils.isEmpty(this.inventoryQueryCode)) {
            map.put("inventoryQueryCode", this.inventoryQueryCode);
        }
        return map;
    }

    public void clearQueryCondition() {
        if (!TextUtils.isEmpty(this.goodsClass)) {
            this.goodsClass = null;
            this.setGoodsClassResult.setValue(new OperateResult(new OperateInUserView(null)));
        }
        if (!TextUtils.isEmpty(this.supplierID)) {
            this.supplierID = null;
            this.setSupplierIdResult.setValue(new OperateResult(new OperateInUserView(null)));
        }
        if (!TextUtils.isEmpty(this.codeStatus)) {
            this.codeStatus = null;
            this.setCodeStatusResult.setValue(new OperateResult(new OperateInUserView(null)));
        }
        if (!TextUtils.isEmpty(this.isInventory)) {
            this.isInventory = null;
            this.setIsInventoryResult.setValue(new OperateResult(new OperateInUserView(null)));
        }
        if (!TextUtils.isEmpty(this.oldGoodsID)) {
            this.oldGoodsID = null;
            this.setOldGoodsIDResult.setValue(new OperateResult(new OperateInUserView(null)));
        }
        if (!TextUtils.isEmpty(this.newGoodsID)) {
            this.newGoodsID = null;
            this.setNewGoodsIDResult.setValue(new OperateResult(new OperateInUserView(null)));
        }
        if (!TextUtils.isEmpty(this.warehousingStartTime)) {
            this.warehousingStartTime = null;
            this.setWarehousingStartTimeResult.setValue(new OperateResult(new OperateInUserView(null)));
        }
        if (!TextUtils.isEmpty(this.warehousingEndTime)) {
            this.warehousingEndTime = null;
            this.setWarehousingEndTimeResult.setValue(new OperateResult(new OperateInUserView(null)));
        }
        if (!TextUtils.isEmpty(this.inventoryQueryCode)) {
            this.inventoryQueryCode = null;
            this.setInventoryQueryCodeResult.setValue(new OperateResult(new OperateInUserView(null)));
        }
    }
}
