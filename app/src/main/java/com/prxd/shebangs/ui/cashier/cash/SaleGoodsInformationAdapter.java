package com.prxd.shebangs.ui.cashier.cash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.prxd.shebangs.R;

import java.text.DecimalFormat;
import java.util.List;

public class SaleGoodsInformationAdapter extends BaseAdapter {

    private Context context;
    private List<GoodsInformation> goodsInformation;
    private OnGoodsInformationChangeListener listener;

    public SaleGoodsInformationAdapter(Context context, List<GoodsInformation> goodsInformation) {
        this.context = context;
        this.goodsInformation = goodsInformation;
    }

    public void setOnGoodsInformationChangeListener(OnGoodsInformationChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return this.goodsInformation.size();
    }

    @Override
    public Object getItem(int position) {
        return this.goodsInformation.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.cashier_list_item, null);
            viewHolder.id = convertView.findViewById(R.id.textView40);
            viewHolder.price = convertView.findViewById(R.id.textView43);
            viewHolder.describe = convertView.findViewById(R.id.textView41);
            viewHolder.num = convertView.findViewById(R.id.textView42);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final GoodsInformation msg = goodsInformation.get(position);
        String id = msg.goodsClassName + "\t(" + msg.id + ")";
        viewHolder.id.setText(id);       //商品名字(商品编号)
        String price = context.getString(R.string.goods_price) + new DecimalFormat("###.00").format(msg.salePrice);
        viewHolder.price.setText(price);  //商品价格
        String describe = context.getString(R.string.goods_status) + msg.isDaiMai + "\t\t" + context.getString(R.string.goods_stock) + msg.stockTotal;
        viewHolder.describe.setText(describe);  //商品颜色 商品尺码 销售状态 库存
        String num = "x" + String.valueOf(msg.num);
        viewHolder.num.setText(num);      //商品数量
        return convertView;
    }

    /**
     * view
     */
    private class ViewHolder {
        private TextView id, price, describe, num;    //条码编号，价格，货物类别，入库时间，扫描时间，条码状态
    }

    /**
     * 商品信息发生改变
     */
    public interface OnGoodsInformationChangeListener {
        //商品信息改变
        void goodsChange(GoodsInformation information);

        //商品被删除
        void goodsDelete(GoodsInformation information);
    }
}
