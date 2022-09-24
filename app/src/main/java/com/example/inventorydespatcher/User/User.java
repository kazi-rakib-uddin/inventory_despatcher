package com.example.inventorydespatcher.User;

import android.content.Context;
import android.content.SharedPreferences;

public class User {


    private String company_code, user_id,email,name;


    private Context context;

    SharedPreferences sharedPreferences;


    public User(Context context) {

        this.context=context;

        sharedPreferences=context.getSharedPreferences("login_details", Context.MODE_PRIVATE);

    }


    public String getEmail() {
        email=sharedPreferences.getString("email","");
        return email;
    }

    public void setEmail(String email) {
        sharedPreferences.edit().putString("email",email).commit();
        this.email = email;
    }


    public String getCompany_code() {

        company_code=sharedPreferences.getString("company_code","");
        return company_code;
    }

    public void setCompany_code(String company_code) {

        sharedPreferences.edit().putString("company_code",company_code).commit();
        this.company_code = company_code;
    }

    public String getUser_id() {

        user_id=sharedPreferences.getString("user_id","");
        return user_id;
    }

    public void setUser_id(String user_id) {

        sharedPreferences.edit().putString("user_id",user_id).commit();
        this.user_id = user_id;
    }

    public  void  remove(){

        sharedPreferences.edit().clear().commit();

    }
}
