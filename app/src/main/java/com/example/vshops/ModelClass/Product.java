package com.example.vshops.ModelClass;

public class Product {
    public String mProductName,mShopemailID;
    public String mProductID,mProductPrice,mProductDetails,mProductImage;
    public Product(){}
    public Product(String name,String ID,String price,String details,String img,String shopemailID) {
        mProductName = name;
        mProductDetails=details;
        mProductID=ID;
        mProductImage=img;
        mProductPrice=price;
        mShopemailID=shopemailID;
    }
}
