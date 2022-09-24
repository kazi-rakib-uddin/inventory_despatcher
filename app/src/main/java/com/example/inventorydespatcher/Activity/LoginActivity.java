package com.example.inventorydespatcher.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.inventorydespatcher.ApiClient.ApiClient;
import com.example.inventorydespatcher.Interface.MyInterface;
import com.example.inventorydespatcher.User.User;
import com.example.inventorydespatcher.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    MyInterface myInterface;
    User user;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myInterface= ApiClient.getApiClient().create(MyInterface.class);
        user = new User(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        if (user.getUser_id()!="")
        {
            startActivity(new Intent(LoginActivity.this,HomePageActivity.class));
            finish();
        }

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.email.getText().toString().equals(""))
                {
                    Toast.makeText(LoginActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                }
                else if (binding.password.getText().toString().equals(""))
                {
                    Toast.makeText(LoginActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    login();
                }
            }
        });
    }

    private void login()
    {
        Call<String> call = myInterface.login(binding.email.getText().toString(), binding.password.getText().toString());
        progressDialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    if (jsonObject.getString("rec").equals("1"))
                    {
                        user.setUser_id(jsonObject.getString("user_id"));
                        user.setEmail(binding.email.getText().toString());

                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        startActivity(new Intent(LoginActivity.this,HomePageActivity.class));
                        finishAffinity();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Invalid login details", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {

                    Toast.makeText(LoginActivity.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(LoginActivity.this, "Slow Network", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}