package com.example.inventorydespatcher.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.inventorydespatcher.ApiClient.ApiClient;
import com.example.inventorydespatcher.Interface.MyInterface;
import com.example.inventorydespatcher.R;
import com.example.inventorydespatcher.User.User;
import com.example.inventorydespatcher.databinding.ActivityHomePageBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageActivity extends AppCompatActivity {

    ActivityHomePageBinding binding;
    User user;
    MyInterface myInterface;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myInterface = ApiClient.getApiClient().create(MyInterface.class);
        user = new User(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);


        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            binding.txtGoodMorning.setText("Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            binding.txtGoodMorning.setText("Good Afternoon");

        }else if(timeOfDay >= 16 && timeOfDay < 21){

            binding.txtGoodMorning.setText("Good Evening");

        }else if(timeOfDay >= 21 && timeOfDay < 24){

            binding.txtGoodMorning.setText("Good Night");

        }


        binding.stocks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomePageActivity.this,OrderHistoryActivity.class));
                //finish();
            }
        });


        binding.cartProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomePageActivity.this,CartActivity.class));
                //finish();
            }
        });


        binding.scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomePageActivity.this,MainActivity.class));
                //finish();
            }
        });



        binding.viewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomePageActivity.this,ViewOrderActivity.class));
                //finish();
            }
        });


        binding.viewRetailerOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomePageActivity.this,ViewRetailerOrderActivity.class));
                //finish();
            }
        });


        fetchProfileDetails();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_log_out, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.menu_log_out:
                user.remove();
                startActivity(new Intent(HomePageActivity.this,LoginActivity.class));
                finishAffinity();

                break;

        }
        return true;
    }



    private void fetchProfileDetails()
    {
        Call<String> call = myInterface.fetch_profile(user.getUser_id());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {

                        binding.userName.setText(jsonObject.getString("name"));
                        binding.email.setText(jsonObject.getString("email"));
                        progressDialog.dismiss();

                    }
                    else
                    {
                        //Toast.makeText(HomePageActivity.this, "Details not found", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(HomePageActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(HomePageActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    @Override
    public void onBackPressed() {

        finishAffinity();
    }
}