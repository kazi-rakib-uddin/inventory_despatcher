package com.example.inventorydespatcher.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.inventorydespatcher.Adapter.OrderHistoryAdapter;
import com.example.inventorydespatcher.Adapter.ViewRetailerOrderAdapter;
import com.example.inventorydespatcher.ApiClient.ApiClient;
import com.example.inventorydespatcher.Interface.MyInterface;
import com.example.inventorydespatcher.Model.Shop_Model;
import com.example.inventorydespatcher.Model.View_Retailer_Order_Model;
import com.example.inventorydespatcher.R;
import com.example.inventorydespatcher.User.User;
import com.example.inventorydespatcher.databinding.ActivityViewRetailerOrderBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewRetailerOrderActivity extends AppCompatActivity {

    ActivityViewRetailerOrderBinding binding;
    ArrayList<View_Retailer_Order_Model> arrayList_order = new ArrayList<>();
    MyInterface myInterface;
    ProgressDialog progressDialog;
    User user;
    String[] result_shop = {""};
    ArrayList<Shop_Model> shop_models;
    String shop_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewRetailerOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        user = new User(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, result_shop);
        binding.spinnerShop.setAdapter(arrayAdapter);


        binding.txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Calendar mcurrentDate = Calendar.getInstance();

                int mDay   = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mYear  = mcurrentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewRetailerOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
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

                                fetchsOrderHistoryDateWise();

                            }
                            else if (month >= 10 && dayOfMonth < 10) {

                                fmonth = "0" + month;
                                month = Integer.parseInt(fmonth) + 1;
                                fDate = "0" + dayOfMonth;
                                String paddedMonth = String.format("%02d", month);
                                binding.txtDate.setText(year + "-" + paddedMonth + "-" + fDate);

                                fetchsOrderHistoryDateWise();
                            }
                            else {

                                fmonth = "0" + month;
                                month = Integer.parseInt(fmonth) + 1;
                                String paddedMonth = String.format("%02d", month);
                                binding.txtDate.setText(year + "-" + paddedMonth + "-" + dayOfMonth);

                                fetchsOrderHistoryDateWise();
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


        binding.spinnerShop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                binding.txtDate.setText("Select Date");

                Shop_Model model = (Shop_Model) adapterView.getItemAtPosition(i);

                shop_id = model.getShop_id();

                fetchsOrderHistoryRetailerWise();
            }
        });


    }



    private void fetchsOrderHistoryDateWise()
    {

        Call<String> call = myInterface.fetch_retailer_order_date_wise(binding.txtDate.getText().toString());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length()==0)
                    {
                        binding.rvOrderHistory.setVisibility(View.GONE);
                        Toast.makeText(ViewRetailerOrderActivity.this, "No Product", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else
                    {
                        arrayList_order.clear();
                        binding.rvOrderHistory.setVisibility(View.VISIBLE);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String invoice_no = jsonObject.getString("invoice_no");
                            String created_at = jsonObject.getString("created_at");
                            String status = jsonObject.getString("status");

                            View_Retailer_Order_Model model = new View_Retailer_Order_Model(invoice_no,created_at,status);
                            arrayList_order.add(model);
                        }


                        binding.rvOrderHistory.setAdapter(new ViewRetailerOrderAdapter(ViewRetailerOrderActivity.this,arrayList_order));
                        progressDialog.dismiss();

                    }

                } catch (JSONException e) {

                    Toast.makeText(ViewRetailerOrderActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(ViewRetailerOrderActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    private void fetchsOrderHistoryRetailerWise()
    {

        Call<String> call = myInterface.fetch_retailer_order_retailer_wise(shop_id);
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length()==0)
                    {
                        binding.rvOrderHistory.setVisibility(View.GONE);
                        Toast.makeText(ViewRetailerOrderActivity.this, "No Product", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else
                    {
                        arrayList_order.clear();
                        binding.rvOrderHistory.setVisibility(View.VISIBLE);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String invoice_no = jsonObject.getString("invoice_no");
                            String created_at = jsonObject.getString("created_at");
                            String status = jsonObject.getString("status");

                            View_Retailer_Order_Model model = new View_Retailer_Order_Model(invoice_no,created_at,status);
                            arrayList_order.add(model);
                        }


                        binding.rvOrderHistory.setAdapter(new ViewRetailerOrderAdapter(ViewRetailerOrderActivity.this,arrayList_order));
                        progressDialog.dismiss();

                    }

                } catch (JSONException e) {

                    Toast.makeText(ViewRetailerOrderActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(ViewRetailerOrderActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
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
                        ArrayAdapter<Shop_Model> adapter = new ArrayAdapter<Shop_Model>(ViewRetailerOrderActivity.this, android.R.layout.simple_dropdown_item_1line, shop_models);
                        binding.spinnerShop.setAdapter(adapter);

                    }

                } catch (JSONException e) {

                    Toast.makeText(ViewRetailerOrderActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(ViewRetailerOrderActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }


}