package com.mycompany.grocerystoreadmin.Model;

public class AboutShop {
    String name, address, email, phone;

    public AboutShop() {
    }

    public AboutShop(String name, String address, String email, String phone) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
