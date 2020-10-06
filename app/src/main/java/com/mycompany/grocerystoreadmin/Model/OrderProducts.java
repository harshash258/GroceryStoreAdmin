package com.mycompany.grocerystoreadmin.Model;

public class OrderProducts {
    String name, price, imageURL, productId;
    int quantity;

    public OrderProducts(){}

    public OrderProducts(String name, int quantity, String price, String imageURL, String productId) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.imageURL = imageURL;
        this.productId = productId;

    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getProductId() {
        return productId;
    }
}
