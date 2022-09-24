package com.example.inventorydespatcher.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.inventorydespatcher.ApiClient.ApiClient;
import com.example.inventorydespatcher.Interface.MyInterface;
import com.example.inventorydespatcher.Model.Shop_Model;
import com.example.inventorydespatcher.R;
import com.example.inventorydespatcher.User.User;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;
    Button btn_add_to_cart, btn_cancel;
    String[] result_admin = {""};
    String[] result_shop = {""};
    BetterSpinner spinner_from, spinner_to;
    LinearLayout lin_quan;
    ArrayList<Shop_Model> shop_models;
    MyInterface myInterface;
    User user;
    ProgressDialog progressDialog;
    TextView txt_product_name, txt_cart_count;
    EditText et_quantity;
    ImageView img_product;
    String hold_image, hold_product_id, from_shop_name="", to_shop_name="", from_shop_id, to_shop_id;
    private static final String img_url = "http://tiwaryleather.com/New_Inventory/public/product_pic/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_main);*/
        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);

        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        user = new User(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);


        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();



    }


    @Override
    public void handleResult(Result rawResult) {


        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_item);
        dialog.setCancelable(false);
        btn_add_to_cart = dialog.findViewById(R.id.btn_add_to_cart);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        spinner_from = dialog.findViewById(R.id.spinner_from);
        spinner_to = dialog.findViewById(R.id.spinner_to);
        lin_quan = dialog.findViewById(R.id.lin_quan);

        btn_add_to_cart = dialog.findViewById(R.id.btn_add_to_cart);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        txt_product_name = dialog.findViewById(R.id.product_name);
        et_quantity = dialog.findViewById(R.id.et_quantity);
        img_product = dialog.findViewById(R.id.img_product);

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, result_admin);
       spinner_from.setAdapter(arrayAdapter1);

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, result_shop);
        spinner_to.setAdapter(arrayAdapter2);

        fetchShop();

        spinner_from.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Shop_Model model = (Shop_Model) adapterView.getItemAtPosition(i);

                from_shop_name = model.getShop_name();
                from_shop_id = model.getShop_id();
            }
        });


        spinner_to.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Shop_Model model = (Shop_Model) adapterView.getItemAtPosition(i);

                to_shop_name = model.getShop_name();
                to_shop_id = model.getShop_id();
            }
        });


        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               if (et_quantity.getText().toString().equals(""))
               {
                   Toast.makeText(MainActivity.this, "Quantity is required", Toast.LENGTH_SHORT).show();
               }
                else if (from_shop_name.equals(""))
                {
                    Toast.makeText(MainActivity.this, "Please Select From Shop", Toast.LENGTH_SHORT).show();
                }
               else if (to_shop_name.equals(""))
               {
                   Toast.makeText(MainActivity.this, "Please Select To Shop", Toast.LENGTH_SHORT).show();
               }
               else if (from_shop_name.equals(to_shop_name))
               {
                   Toast.makeText(MainActivity.this, "Please Select Another Shop", Toast.LENGTH_SHORT).show();
               }
               else
               {

                   addProduct(rawResult.getText(),et_quantity.getText().toString(),dialog);

               }

            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                scannerView.setResultHandler(MainActivity.this::handleResult);
                scannerView.startCamera();

            }
        });


        fetchProduct(rawResult.getText(), dialog);

        dialog.show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem menuItem = menu.findItem(R.id.menu_cart);
        View view = menuItem.getActionView();
        txt_cart_count = view.findViewById(R.id.txt_cart_count);
        txt_cart_count.setText("0");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,CartActivity.class));
            }
        });

        fetchCount(txt_cart_count);

        return true;
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
                        ArrayAdapter<Shop_Model> adapter = new ArrayAdapter<Shop_Model>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, shop_models);
                        spinner_from.setAdapter(adapter);
                        spinner_to.setAdapter(adapter);
                    }

                } catch (JSONException e) {

                    Toast.makeText(MainActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void fetchProduct(String qr_code_no, Dialog dialog)
    {
        Call<String> call = myInterface.fetch_product(qr_code_no);
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        txt_product_name.setText(jsonObject.getString("prod_name"));
                        hold_product_id= jsonObject.getString("id");

                        hold_image = jsonObject.getString("image");

                        String img = img_url+jsonObject.getString("image");

                        Glide.with(MainActivity.this)
                                .load(img)
                                .into(img_product);


                        progressDialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Product Not Found", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        dialog.dismiss();

                       /* scannerView.setResultHandler(MainActivity.this::handleResult);
                        scannerView.startCamera();*/
                    }

                } catch (JSONException e) {

                    Toast.makeText(MainActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }



    private void addProduct(String qr_code_no, String quantity, Dialog dialog)
    {


        Call<String> call = myInterface.add_product(qr_code_no,hold_product_id,user.getUser_id(),from_shop_id,to_shop_id,quantity);
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {

                        Toast.makeText(MainActivity.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        dialog.dismiss();
                        scannerView.setResultHandler(MainActivity.this::handleResult);
                        scannerView.startCamera();

                        fetchCount(txt_cart_count);

                    }
                    else if (jsonObject.getString("rec").equals("2"))
                    {
                        Toast.makeText(MainActivity.this, "Product Already Add", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    else if (jsonObject.getString("rec").equals("3"))
                    {
                        String stocks = jsonObject.getString("quantity");

                        Toast.makeText(MainActivity.this, stocks+ " Product Available", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    else if (jsonObject.getString("rec").equals("4"))
                    {
                        Toast.makeText(MainActivity.this, "Stocks not available", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    else
                    {
                        Toast.makeText(MainActivity.this, "Not Add", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        dialog.dismiss();

                        scannerView.setResultHandler(MainActivity.this::handleResult);
                        scannerView.startCamera();
                    }

                } catch (JSONException e) {

                    Toast.makeText(MainActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    private void fetchCount(TextView txt_cart_count)
    {
        Call<String> call = myInterface.fetch_count(user.getUser_id());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        txt_cart_count.setText(jsonObject.getString("count"));
                    }
                    else
                    {
                        txt_cart_count.setText("0");
                    }

                } catch (JSONException e) {

                    Toast.makeText(MainActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
            }
        });
    }


}