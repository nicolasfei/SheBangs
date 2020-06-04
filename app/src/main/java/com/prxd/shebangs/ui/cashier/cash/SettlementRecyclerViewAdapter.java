package com.prxd.shebangs.ui.cashier.cash;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prxd.shebangs.R;

import java.util.List;

public class SettlementRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SettlementRVAdapter";
    private Context context;
    private List<SettlementGoodsMsg> content;

    public SettlementRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setContent(List<SettlementGoodsMsg> content) {
        this.content = content;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SettlementGoodsHolder(LayoutInflater.from(context).inflate(R.layout.settlement_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SettlementGoodsMsg msg = content.get(position);
        ((SettlementGoodsHolder) holder).type.setText(msg.type);
        Log.d(TAG, "onBindViewHolder: "+position);
        if (position == 0) {
            ((SettlementGoodsHolder) holder).num.setText(msg.num);
            ((SettlementGoodsHolder) holder).price.setText(msg.price);
        } else {
            ((SettlementGoodsHolder) holder).num.setText(("x" + msg.num));
            ((SettlementGoodsHolder) holder).price.setText((context.getString(R.string.goods_price) + msg.price));
        }
    }

    @Override
    public int getItemCount() {
        return content == null ? 0 : content.size();
    }

    public class SettlementGoodsHolder extends RecyclerView.ViewHolder {
        private TextView type;
        private TextView num;
        private TextView price;

        public SettlementGoodsHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.textView12);
            num = itemView.findViewById(R.id.textView13);
            price = itemView.findViewById(R.id.textView14);
        }
    }
}
