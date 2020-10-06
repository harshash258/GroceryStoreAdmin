package com.mycompany.grocerystoreadmin.Model;

public class OrderDetails {
    String orderId;
    String consumer;
    String userId;
    String date, time;
    int total;


    public String getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public OrderDetails(String orderId, String consumer, String userId, String date, String time, int total) {
        this.orderId = orderId;
        this.consumer = consumer;
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.total = total;
    }

    public OrderDetails() {
    }

    public String getConsumer() {
        return consumer;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getTotal() {
        return total;
    }

}
