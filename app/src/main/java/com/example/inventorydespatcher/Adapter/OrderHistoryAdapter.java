package com.example.inventorydespatcher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.inventorydespatcher.Model.CartModel;
import com.example.inventorydespatcher.R;
import com.example.inventorydespatcher.databinding.CustomOrdersBinding;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    ArrayList<CartModel> arrayList_catagory;
    Context context;

    public OrderHistoryAdapter(Context context, ArrayList<CartModel> arrayList_catagory) {
        this.arrayList_catagory = arrayList_catagory;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_orders,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


       /* Glide
                .with(holder.itemView)
                .load(arrayList_catagory.get(position).getImage())
                .centerCrop()
                .into(holder.binding.image);*/



    }

    @Override
    public int getItemCount() {
        return arrayList_catagory.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CustomOrdersBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomOrdersBinding.bind(itemView);

        }
    }
}
