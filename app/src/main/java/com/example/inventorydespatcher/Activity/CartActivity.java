package com.example.inventorydespatcher.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.inventorydespatcher.Adapter.CartAdapter;
import com.example.inventorydespatcher.ApiClient.ApiClient;
import com.example.inventorydespatcher.Interface.MyInterface;
import com.example.inventorydespatcher.Model.CartModel;
import com.example.inventorydespatcher.User.User;
import com.example.inventorydespatcher.databinding.ActivityCartBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements CartAdapter.btnRemoveInterface {

    ActivityCartBinding binding;
    ArrayList<CartModel> cartModels = new ArrayList<>();
    MyInterface myInterface;
    User user;
    ProgressDialog progressDialog;
    private static final String img_url = "http://tiwaryleather.com/New_Inventory/public/product_pic/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        user = new User(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);


        fetchCart();

        binding.btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                transferOrder();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void fetchCart()
    {

        Call<String> call = myInterface.fetch_cart(user.getUser_id());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length()==0)
                    {
                        binding.rvCart.setVisibility(View.GONE);
                        binding.btnTransfer.setVisibility(View.GONE);
                        //Toast.makeText(SaleCartActivity.this, "No Product", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else
                    {

                        cartModels.clear();
                        binding.rvCart.setVisibility(View.VISIBLE);
                        binding.btnTransfer.setVisibility(View.VISIBLE);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String product_id = jsonObject.getString("product_id");
                            String qr_code_no = jsonObject.getString("qr_code_no");
                            String product_name = jsonObject.getString("product_name");
                            String from_shop_id = jsonObject.getString("from_shop_id");
                            String to_shop_id = jsonObject.getString("to_shop_id");
                            String quantity = jsonObject.getString("quantity");
                            String from_shop_name = jsonObject.getString("from_shop_name");
                            String to_shop_name = jsonObject.getString("to_shop_name");
                            String product_img =img_url + jsonObject.getString("product_img");

                            CartModel model = new CartModel(qr_code_no,from_shop_id,to_shop_id,product_id,product_name,quantity,from_shop_name,to_shop_name,product_img);
                            cartModels.add(model);

                        }

                        binding.rvCart.setAdapter(new CartAdapter(CartActivity.this,cartModels,CartActivity.this));
                        progressDialog.dismiss();

                    }

                } catch (JSONException e) {

                    Toast.makeText(CartActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(CartActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    @Override
    public void removeProduct(String qrCodeNo) {

        deleteCartProduct(qrCodeNo);
    }




    private void deleteCartProduct(String qr_code_no)
    {
        Call<String> call = myInterface.remove_cart_product(qr_code_no);
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        Toast.makeText(CartActivity.this, "Removed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        fetchCart();
                    }
                    else
                    {
                        Toast.makeText(CartActivity.this, "Not Removed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(CartActivity.this, "somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(CartActivity.this, "slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    private void transferOrder()
    {
        Call<String> call = myInterface.transfer_order(user.getUser_id());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        Toast.makeText(CartActivity.this, "Order Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        fetchCart();
                    }
                    else if (jsonObject.getString("rec").equals("2"))
                    {
                        progressDialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(CartActivity.this, "Not Order", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(CartActivity.this, "somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(CartActivity.this, "slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}