package com.example.inventorydespatcher.Model;

public class View_Order_Model {

    String product_name, image, from_shop_name, to_shop_name, quantity;

    public View_Order_Model(String product_name, String image, String from_shop_name, String to_shop_name, String quantity) {
        this.product_name = product_name;
        this.image = image;
        this.from_shop_name = from_shop_name;
        this.to_shop_name = to_shop_name;
        this.quantity = quantity;
    }


    public String getProduct_name() {
        return product_name;
    }

    public String getImage() {
        return image;
    }

    public String getFrom_shop_name() {
        return from_shop_name;
    }

    public String getTo_shop_name() {
        return to_shop_name;
    }

    public String getQuantity() {
        return quantity;
    }
}
