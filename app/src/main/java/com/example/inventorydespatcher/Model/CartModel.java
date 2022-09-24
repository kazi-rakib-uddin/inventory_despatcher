package com.example.inventorydespatcher.Model;

public class CartModel {

    String qr_code_no, from_shop_id, to_shop_id, product_id, product_name, quantity, from_shop_name, to_shop_name, image;

    public CartModel(String qr_code_no, String from_shop_id, String to_shop_id, String product_id, String product_name, String quantity, String from_shop_name, String to_shop_name, String image) {
        this.qr_code_no = qr_code_no;
        this.from_shop_id = from_shop_id;
        this.to_shop_id = to_shop_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.quantity = quantity;
        this.from_shop_name = from_shop_name;
        this.to_shop_name = to_shop_name;
        this.image = image;
    }


    public String getQr_code_no() {
        return qr_code_no;
    }

    public String getFrom_shop_id() {
        return from_shop_id;
    }

    public String getTo_shop_id() {
        return to_shop_id;
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

    public String getFrom_shop_name() {
        return from_shop_name;
    }

    public String getTo_shop_name() {
        return to_shop_name;
    }

    public String getImage() {
        return image;
    }
}
