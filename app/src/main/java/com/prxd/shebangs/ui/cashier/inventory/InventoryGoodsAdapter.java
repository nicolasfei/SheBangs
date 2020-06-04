package com.prxd.shebangs.ui.cashier.inventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.prxd.shebangs.R;
import java.util.List;

public class InventoryGoodsAdapter extends BaseAdapter {

    private Context context;
    private List<InventoryGoodsInformation> goodsList;

    public InventoryGoodsAdapter(Context context, List<InventoryGoodsInformation> goodsList) {
        this.context = context;
        this.goodsList = goodsList;
    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final InventoryGoodsAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new InventoryGoodsAdapter.ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.inventory_goods_list_item, null);
            viewHolder.head = convertView.findViewById(R.id.head);
            viewHolder.describe = convertView.findViewById(R.id.describe);
            viewHolder.total = convertView.findViewById(R.id.total);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (InventoryGoodsAdapter.ViewHolder) convertView.getTag();
        }

        final InventoryGoodsInformation msg = goodsList.get(position);
        String headValue = msg.goodsClassName + "\t(" + msg.k_j_BarCode_Id + ")" + "\t\t\t\t\t\t\t\t\t\t" + msg.stateName;
        viewHolder.head.setText(headValue);         //商品名字(商品编号)      状态
        String describe = context.getString(R.string.goods_in_warehouse_time) + ":" + msg.inTime + "\n" + context.getString(R.string.goods_scan_time) + msg.getTime;
        viewHolder.describe.setText(describe);      //入库时间，扫描时间
        String totalValue = context.getString(R.string.goods_num) + ":x1" + "\t\t\t\t\t\t\t\t\t\t" + context.getString(R.string.total) + ":" +
                context.getString(R.string.goods_price) + msg.getFormatSalePrice();
        viewHolder.total.setText(totalValue);      //商品统计
        return convertView;
    }

    /**
     * view
     */
    private class ViewHolder {
        private ImageView photo;                    //图片
        private TextView head, describe, total;     //头，描述，合计
    }
}
