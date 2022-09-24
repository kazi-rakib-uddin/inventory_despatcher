package com.example.inventorydespatcher.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventorydespatcher.Adapter.ViewOrderAdapter;
import com.example.inventorydespatcher.ApiClient.ApiClient;
import com.example.inventorydespatcher.Interface.MyInterface;
import com.example.inventorydespatcher.Model.Shop_Model;
import com.example.inventorydespatcher.Model.View_Order_Model;
import com.example.inventorydespatcher.R;
import com.example.inventorydespatcher.User.User;
import com.example.inventorydespatcher.databinding.ActivityViewOrderBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewOrderActivity extends AppCompatActivity {

    MyInterface myInterface;
    User user;
    ProgressDialog progressDialog;
    ActivityViewOrderBinding binding;
    ArrayList<View_Order_Model> arrayList_view_order;
    private static final String img_url = "http://tiwaryleather.com/New_Inventory/public/product_pic/";
    String[] result_admin = {""};
    String[] result_shop = {""};
    ArrayList<Shop_Model> shop_models;
    String from_shop_name="", to_shop_name="", from_shop_id, to_shop_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        user = new User(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);


        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, result_admin);
        binding.spinnerFrom.setAdapter(arrayAdapter1);

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, result_shop);
        binding.spinnerTo.setAdapter(arrayAdapter2);


        binding.txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.spinnerFrom.setHint("From Shop");
                binding.spinnerTo.setHint("To Shop");

                Calendar mcurrentDate = Calendar.getInstance();

                int mDay   = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mYear  = mcurrentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String fmonth, fDate;


                        try {
                            if (month < 10 && dayOfMonth < 10) {

                                fmonth = "0" + month;
                                month = Integer.parseInt(fmonth) + 1;
                                fDate = "0" + dayOfMonth;
                                String paddedMonth = String.format("%02d", month);
                                binding.txtDate.setText(year + "-" + paddedMonth + "-" + fDate);

                                fetchOrderDateWise();

                            }
                            else if (month >= 10 && dayOfMonth < 10) {

                                fmonth = "0" + month;
                                month = Integer.parseInt(fmonth) + 1;
                                fDate = "0" + dayOfMonth;
                                String paddedMonth = String.format("%02d", month);
                                binding.txtDate.setText(year + "-" + paddedMonth + "-" + fDate);

                                fetchOrderDateWise();
                            }
                            else {

                                fmonth = "0" + month;
                                month = Integer.parseInt(fmonth) + 1;
                                String paddedMonth = String.format("%02d", month);
                                binding.txtDate.setText(year + "-" + paddedMonth + "-" + dayOfMonth);

                                fetchOrderDateWise();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.show();



            }
        });


        fetchShop();


        binding.spinnerFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                binding.txtDate.setText("Select Date");

                Shop_Model model = (Shop_Model) adapterView.getItemAtPosition(i);

                from_shop_name = model.getShop_name();
                from_shop_id = model.getShop_id();
            }
        });



        binding.spinnerTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                binding.txtDate.setText("Select Date");

                Shop_Model model = (Shop_Model) adapterView.getItemAtPosition(i);

                to_shop_name = model.getShop_name();
                to_shop_id = model.getShop_id();

                fetchOrderRetailerWise();
            }
        });



    }


    private void fetchOrderDateWise()
    {
        arrayList_view_order = new ArrayList<>();

        Call<String> call = myInterface.fetch_order_date_wise(user.getUser_id(), binding.txtDate.getText().toString());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length()==0)
                    {
                        Toast.makeText(ViewOrderActivity.this, "No Product", Toast.LENGTH_SHORT).show();
                        binding.rvViewOrder.setVisibility(View.GONE);
                        progressDialog.dismiss();
                    }
                    else
                    {
                        arrayList_view_order.clear();
                        binding.rvViewOrder.setVisibility(View.VISIBLE);

                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String product_name = jsonObject.getString("product_name");
                            String product_img =img_url + jsonObject.getString("product_img");
                            String from_shop = jsonObject.getString("from_shop");
                            String to_shop = jsonObject.getString("to_shop");
                            String quantity = jsonObject.getString("quantity");

                            View_Order_Model model = new View_Order_Model(product_name,product_img,from_shop,to_shop,quantity);
                            arrayList_view_order.add(model);
                        }

                        binding.rvViewOrder.setAdapter(new ViewOrderAdapter(ViewOrderActivity.this,arrayList_view_order));
                        progressDialog.dismiss();

                    }


                } catch (JSONException e) {

                    Toast.makeText(ViewOrderActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(ViewOrderActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    private void fetchOrderRetailerWise()
    {
        arrayList_view_order = new ArrayList<>();

        Call<String> call = myInterface.fetch_order_retailer_wise(user.getUser_id(), from_shop_id,to_shop_id);
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length()==0)
                    {
                        Toast.makeText(ViewOrderActivity.this, "No Product", Toast.LENGTH_SHORT).show();
                        binding.rvViewOrder.setVisibility(View.GONE);
                        progressDialog.dismiss();
                    }
                    else
                    {
                        arrayList_view_order.clear();
                        binding.rvViewOrder.setVisibility(View.VISIBLE);

                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String product_name = jsonObject.getString("product_name");
                            String product_img =img_url + jsonObject.getString("product_img");
                            String from_shop = jsonObject.getString("from_shop");
                            String to_shop = jsonObject.getString("to_shop");
                            String quantity = jsonObject.getString("quantity");

                            View_Order_Model model = new View_Order_Model(product_name,product_img,from_shop,to_shop,quantity);
                            arrayList_view_order.add(model);
                        }

                        binding.rvViewOrder.setAdapter(new ViewOrderAdapter(ViewOrderActivity.this,arrayList_view_order));
                        progressDialog.dismiss();

                    }


                } catch (JSONException e) {

                    Toast.makeText(ViewOrderActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(ViewOrderActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    private void fetchShop()
    {
        shop_models = new ArrayList<>();
        Call<String> call = myInterface.fetch_shop();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length()==0)
                    {

                    }
                    else
                    {

                        for (int i =0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String id = jsonObject.getString("id");
                            String shop_name = jsonObject.getString("shop_name");

                            Shop_Model model = new Shop_Model(id,shop_name);
                            shop_models.add(model);
                        }
                        ArrayAdapter<Shop_Model> adapter = new ArrayAdapter<Shop_Model>(ViewOrderActivity.this, android.R.layout.simple_dropdown_item_1line, shop_models);
                        binding.spinnerFrom.setAdapter(adapter);
                        binding.spinnerTo.setAdapter(adapter);
                    }

                } catch (JSONException e) {

                    Toast.makeText(ViewOrderActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(ViewOrderActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }

}