package com.example.inventorydespatcher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.inventorydespatcher.Model.Sale_All_Product_Model;
import com.example.inventorydespatcher.R;
import com.example.inventorydespatcher.databinding.CustomSaleAllProductBinding;

import java.util.ArrayList;

public class OrderAllProductAdapter extends RecyclerView.Adapter<OrderAllProductAdapter.MyViewHolder> {

    ArrayList<Sale_All_Product_Model> orderCartModels;
    Context context;

    public OrderAllProductAdapter(Context context, ArrayList<Sale_All_Product_Model> orderCartModels) {
        this.orderCartModels = orderCartModels;
        this.context=context;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_sale_all_product,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.productName.setText(orderCartModels.get(position).getProduct_name());
        //holder.binding.qrCodeNo.setText("QR Code No : "+orderCartModels.get(position).getQr_code_no());
        //holder.binding.price.setText("₹ "+orderCartModels.get(position).getPrice());
        holder.binding.quantity.setText("QTY : "+orderCartModels.get(position).getQuantity());



        Glide.with(context)
                .load(orderCartModels.get(position).getImage())
                .into(holder.binding.image);



    }



    @Override
    public int getItemCount() {
        return orderCartModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CustomSaleAllProductBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomSaleAllProductBinding.bind(itemView);

        }
    }
}
