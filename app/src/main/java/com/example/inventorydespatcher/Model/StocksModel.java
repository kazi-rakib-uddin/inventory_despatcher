package com.example.inventorydespatcher.Model;

public class StocksModel {

    String product_id, product_name, quantity, image, retailer_name;

    public StocksModel(String product_id, String product_name, String quantity, String image, String retailer_name) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.quantity = quantity;
        this.image = image;
        this.retailer_name = retailer_name;
    }


    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getImage() {
        return image;
    }

    public String getRetailer_name() {
        return retailer_name;
    }
}
