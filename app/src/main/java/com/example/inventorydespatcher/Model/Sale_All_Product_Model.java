package com.example.inventorydespatcher.Model;

public class Sale_All_Product_Model {

    String product_name, quantity, price, image;

    public Sale_All_Product_Model(String product_name, String quantity, String price, String image) {
        this.product_name = product_name;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }
}
