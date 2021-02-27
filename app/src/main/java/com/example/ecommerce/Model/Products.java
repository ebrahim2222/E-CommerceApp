package com.example.ecommerce.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Products implements Parcelable {
    private String id;
    private String pName,pDesc,pPrice,pTime,pDate,pImage,category,key,quantity;

    public Products() {
    }

    public Products(String id, String pName, String pDesc, String pPrice, String pTime, String pDate, String pImage, String category, String key) {
        this.id = id;
        this.pName = pName;
        this.pDesc = pDesc;
        this.pPrice = pPrice;
        this.pTime = pTime;
        this.pDate = pDate;
        this.pImage = pImage;
        this.category = category;
        this.key = key;
    }

    public Products(String id, String pName, String pDesc, String pPrice, String pTime, String pDate, String pImage, String category, String key, String quantity) {
        this.id = id;
        this.pName = pName;
        this.pDesc = pDesc;
        this.pPrice = pPrice;
        this.pTime = pTime;
        this.pDate = pDate;
        this.pImage = pImage;
        this.category = category;
        this.key = key;
        this.quantity = quantity;
    }

    public Products(String id, String pName, String pDesc, String pPrice, String pTime, String pDate, String pImage, String category) {
        this.id = id;
        this.pName = pName;
        this.pDesc = pDesc;
        this.pPrice = pPrice;
        this.pTime = pTime;
        this.pDate = pDate;
        this.pImage = pImage;
        this.category = category;
    }

    protected Products(Parcel in) {
        id = in.readString();
        pName = in.readString();
        pDesc = in.readString();
        pPrice = in.readString();
        pTime = in.readString();
        pDate = in.readString();
        pImage = in.readString();
        category = in.readString();
    }

    public static final Creator<Products> CREATOR = new Creator<Products>() {
        @Override
        public Products createFromParcel(Parcel in) {
            return new Products(in);
        }

        @Override
        public Products[] newArray(int size) {
            return new Products[size];
        }


    };

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpDesc() {
        return pDesc;
    }

    public void setpDesc(String pDesc) {
        this.pDesc = pDesc;
    }

    public String getpPrice() {
        return pPrice;
    }

    public void setpPrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getpDate() {
        return pDate;
    }

    public void setpDate(String pDate) {
        this.pDate = pDate;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(pName);
        dest.writeString(pDesc);
        dest.writeString(pPrice);
        dest.writeString(pTime);
        dest.writeString(pDate);
        dest.writeString(pImage);
        dest.writeString(category);
    }
}
