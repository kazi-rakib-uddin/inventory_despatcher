package com.example.inventorydespatcher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inventorydespatcher.Model.StocksModel;
import com.example.inventorydespatcher.Model.View_Order_Model;
import com.example.inventorydespatcher.R;
import com.example.inventorydespatcher.databinding.CustomStocksItemBinding;
import com.example.inventorydespatcher.databinding.CustomViewOrderBinding;

import java.util.ArrayList;

public class ViewOrderAdapter extends RecyclerView.Adapter<ViewOrderAdapter.MyViewHolder> {

    ArrayList<View_Order_Model> view_order_models;
    Context context;

    public ViewOrderAdapter(Context context, ArrayList<View_Order_Model> view_order_models) {
        this.view_order_models = view_order_models;
        this.context=context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_view_order,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.productName.setText(view_order_models.get(position).getProduct_name());
        holder.binding.fromShopName.setText("From Shop : "+view_order_models.get(position).getFrom_shop_name());
        holder.binding.toShopName.setText("To Shop : "+view_order_models.get(position).getTo_shop_name());
        holder.binding.quantity.setText("QTY : "+view_order_models.get(position).getQuantity());



        Glide.with(context)
                .load(view_order_models.get(position).getImage())
                .into(holder.binding.image);




    }



    @Override
    public int getItemCount() {
        return view_order_models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CustomViewOrderBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomViewOrderBinding.bind(itemView);

        }
    }
}
