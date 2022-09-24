package com.example.inventorydespatcher.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.inventorydespatcher.Adapter.OrderHistoryAdapter;
import com.example.inventorydespatcher.Adapter.StocksAdapter;
import com.example.inventorydespatcher.ApiClient.ApiClient;
import com.example.inventorydespatcher.Interface.MyInterface;
import com.example.inventorydespatcher.Model.CartModel;
import com.example.inventorydespatcher.Model.StocksModel;
import com.example.inventorydespatcher.User.User;
import com.example.inventorydespatcher.databinding.ActivityOrderHistoryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    ActivityOrderHistoryBinding binding;
    MyInterface myInterface;
    ProgressDialog progressDialog;
    User user;
    ArrayList<StocksModel> stocksModels;
    private static final String img_url = "http://tiwaryleather.com/New_Inventory/public/product_pic/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        user = new User(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        fetchStocks();

    }


    private void fetchStocks()
    {
        stocksModels = new ArrayList<>();

        Call<String> call = myInterface.fetch_stocks();
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length()==0)
                    {
                        Toast.makeText(OrderHistoryActivity.this, "Stock Not Found", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else
                    {

                        stocksModels.clear();
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String product_id = jsonObject.getString("product_id");
                            String product_name = jsonObject.getString("product_name");
                            String stock = jsonObject.getString("stock");
                            String retailer_name = jsonObject.getString("retailer_name");
                            String img = img_url+jsonObject.getString("image");

                            StocksModel model = new StocksModel(product_id,product_name,stock,img,retailer_name);

                            stocksModels.add(model);
                        }

                        binding.rvOrder.setAdapter(new StocksAdapter(OrderHistoryActivity.this,stocksModels));
                        progressDialog.dismiss();

                    }

                } catch (JSONException e) {

                    Toast.makeText(OrderHistoryActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(OrderHistoryActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}