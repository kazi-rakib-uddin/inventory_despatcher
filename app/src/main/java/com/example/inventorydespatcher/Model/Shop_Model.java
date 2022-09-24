package com.example.inventorydespatcher.Model;

public class Shop_Model {

    String shop_id, shop_name;

    public Shop_Model(String shop_id, String shop_name) {
        this.shop_id = shop_id;
        this.shop_name = shop_name;
    }

    public String getShop_id() {
        return shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    @Override
    public String toString() {
        return shop_name;
    }
}

