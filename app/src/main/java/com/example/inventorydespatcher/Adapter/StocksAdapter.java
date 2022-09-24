package com.example.inventorydespatcher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inventorydespatcher.Model.StocksModel;
import com.example.inventorydespatcher.R;
import com.example.inventorydespatcher.databinding.CustomStocksItemBinding;


import java.util.ArrayList;

public class StocksAdapter extends RecyclerView.Adapter<StocksAdapter.MyViewHolder> {

    ArrayList<StocksModel> arrayList_catagory;
    Context context;
    public static int total=0;

    public StocksAdapter(Context context, ArrayList<StocksModel> arrayList_catagory) {
        this.arrayList_catagory = arrayList_catagory;
        this.context=context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_stocks_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.retailerName.setText(arrayList_catagory.get(position).getRetailer_name());
        holder.binding.productName.setText(arrayList_catagory.get(position).getProduct_name());
        holder.binding.quantity.setText("QTY : "+arrayList_catagory.get(position).getQuantity());



        Glide.with(context)
                .load(arrayList_catagory.get(position).getImage())
                .into(holder.binding.image);




    }



    @Override
    public int getItemCount() {
        return arrayList_catagory.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CustomStocksItemBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomStocksItemBinding.bind(itemView);

        }
    }
}
