package com.example.inventorydespatcher.Interface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MyInterface {

    @FormUrlEncoded
    @POST("login.php")
    Call<String> login(@Field("email") String email, @Field("password") String password);


    @GET("fetch_shop.php")
    Call<String> fetch_shop();

    @FormUrlEncoded
    @POST("fetch_product.php")
    Call<String> fetch_product(@Field("qr_code_no") String qr_code_no);

    @FormUrlEncoded
    @POST("add_product.php")
    Call<String> add_product(@Field("qr_code_no") String qr_code_no, @Field("product_id") String product_id, @Field("dispatcher_id") String dispatcher_id,
                             @Field("from_shop_id") String from_shop_id, @Field("to_shop_id") String to_shop_id, @Field("quantity") String quantity);


    @FormUrlEncoded
    @POST("fetch_count.php")
    Call<String> fetch_count(@Field("dispatcher_id") String dispatcher_id);


    @FormUrlEncoded
    @POST("fetch_cart.php")
    Call<String> fetch_cart(@Field("dispatcher_id") String dispatcher_id);

    @FormUrlEncoded
    @POST("fetch_sale_order_history.php")
    Call<String> fetch_sale_order_history(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("fetch_sale_product.php")
    Call<String> fetch_sale_product(@Field("invoice_no") String invoice_no);


    @FormUrlEncoded
    @POST("remove_cart_product.php")
    Call<String> remove_cart_product(@Field("qr_code_no") String qr_code_no);

    @FormUrlEncoded
    @POST("fetch_profile.php")
    Call<String> fetch_profile(@Field("dispatcher_id") String dispatcher_id);

    @FormUrlEncoded
    @POST("transfer_order.php")
    Call<String> transfer_order(@Field("dispatcher_id") String dispatcher_id);

    @FormUrlEncoded
    @POST("fetch_order_date_wise.php")
    Call<String> fetch_order_date_wise(@Field("dispatcher_id") String dispatcher_id, @Field("date") String date);

    @FormUrlEncoded
    @POST("fetch_order_retailer_wise.php")
    Call<String> fetch_order_retailer_wise(@Field("dispatcher_id") String dispatcher_id, @Field("from_shop_id") String from_shop_id,
                                           @Field("to_shop_id") String to_shop_id);

    @FormUrlEncoded
    @POST("fetch_retailer_order_date_wise.php")
    Call<String> fetch_retailer_order_date_wise(@Field("date") String date);

    @FormUrlEncoded
    @POST("fetch_retailer_order_retailer_wise.php")
    Call<String> fetch_retailer_order_retailer_wise(@Field("retailer_id") String retailer_id);


    @GET("fetch_stocks.php")
    Call<String> fetch_stocks();



    @FormUrlEncoded
    @POST("fetch_order_product.php")
    Call<String> fetch_order_product(@Field("invoice_no") String invoice_no);

}
