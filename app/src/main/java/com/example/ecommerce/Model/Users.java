package com.example.ecommerce.Model;

public class Users {
    private String email,password,phone,image,address,name,key;

    public Users() {
    }

    public Users(String email, String password, String phone, String key) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.key = key;
    }

    public Users(String email, String password, String phone, String image, String address, String name) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.image = image;
        this.address = address;
        this.name = name;
    }


    public Users(String email, String password, String phone, String image, String address, String name, String key) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.image = image;
        this.address = address;
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
