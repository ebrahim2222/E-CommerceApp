package com.example.ecommerce.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Orders implements Parcelable {
    private String name,phone,address,city,date,time,totalPrice,Quantity,key,productsName,productsDesc,shipped,userKey;

    public Orders() {
    }

    public Orders(String name, String phone, String address, String city, String date, String time, String totalPrice, String quantity, String key, String productsName, String productsDesc, String shipped, String userKey) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.date = date;
        this.time = time;
        this.totalPrice = totalPrice;
        Quantity = quantity;
        this.key = key;
        this.productsName = productsName;
        this.productsDesc = productsDesc;
        this.shipped = shipped;
        this.userKey = userKey;
    }

    protected Orders(Parcel in) {
        name = in.readString();
        phone = in.readString();
        address = in.readString();
        city = in.readString();
        date = in.readString();
        time = in.readString();
        totalPrice = in.readString();
        Quantity = in.readString();
        key = in.readString();
        productsName = in.readString();
        productsDesc = in.readString();
        shipped = in.readString();
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public static final Creator<Orders> CREATOR = new Creator<Orders>() {
        @Override
        public Orders createFromParcel(Parcel in) {
            return new Orders(in);
        }

        @Override
        public Orders[] newArray(int size) {
            return new Orders[size];
        }
    };

    public String getProductsName() {
        return productsName;
    }

    public void setProductsName(String productsName) {
        this.productsName = productsName;
    }

    public String getProductsDesc() {
        return productsDesc;
    }

    public void setProductsDesc(String productsDesc) {
        this.productsDesc = productsDesc;
    }

    public String getShipped() {
        return shipped;
    }

    public void setShipped(String shipped) {
        this.shipped = shipped;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(totalPrice);
        dest.writeString(Quantity);
        dest.writeString(key);
        dest.writeString(productsName);
        dest.writeString(productsDesc);
        dest.writeString(shipped);
    }
}
