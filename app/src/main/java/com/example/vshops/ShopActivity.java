package com.example.vshops;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vshops.AdapterClass.ProductAdapter;
import com.example.vshops.ModelClass.Product;
import com.example.vshops.ModelClass.Shop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {

    ListView mListView;
    FloatingActionButton mLocateShopFAB;
    TextView mShopName, mShopOwner, mShopMobile;
    Shop mShop;
    String shopName, shopOwner, shopMobile, shopEmailID;
    GeoPoint mShopGeopoint;
    ProductAdapter productAdapter;
    FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        mShopName = findViewById(R.id.shop_name_others_text_view);
        mShopOwner = findViewById(R.id.shop_owner_others_text_view);
        mShopMobile = findViewById(R.id.shop_mobile_others_text_view);


        shopEmailID = getIntent().getStringExtra("shopEmailID");
        String shopEmailIDFromWA = null;
        if (getIntent().getDataString()!=null) {
            shopEmailIDFromWA = getIntent().getDataString().substring(22);
        }
        if (shopEmailIDFromWA != null) {
            shopEmailID = shopEmailIDFromWA;
        }


        mFirebaseFirestore.collection("users")
                .whereEqualTo(FieldPath.documentId(), shopEmailID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                shopName = document.get("shop_name").toString();
                                shopOwner = document.get("name").toString();
                                shopMobile = document.get("mobile").toString();
                                mShopName.setText(shopName);
                                mShopOwner.setText(shopOwner);
                                mShopMobile.setText("+91-" + shopMobile);
                                mShopGeopoint = document.getGeoPoint("shop_location");
                            }
                        }
                    }
                });

        // Construct the data source
        ArrayList<Product> arrayOfProducts = new ArrayList<Product>();

        mFirebaseFirestore.collection("users")
                .document(shopEmailID)
                .collection("products")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Product product = new Product();
                        product.mProductName = document.get("product_name").toString();
                        product.mProductPrice = document.get("price").toString();
                        product.mProductImage = document.get("image").toString();
                        product.mProductID = document.getId();
                        product.mProductDetails = document.get("details").toString();
                        product.mShopemailID = shopEmailID;
                        arrayOfProducts.add(product);
                    }
                }
                // Create the adapter to convert the array to views
                productAdapter = new ProductAdapter(ShopActivity.this, arrayOfProducts, 0);
                // Attach the adapter to a ListView
                mListView = (ListView) findViewById(R.id.others_shop_list_view);
                mListView.setAdapter(productAdapter);
            }
        });


        //Dummy Code for testing
        /*int i;
        for (i = 1; i <= 15; i++) {
            String s = "Product";
            s = s + Integer.toString(i);
            Product product = new Product(s);
            arrayOfProducts.add(product);
        }*/






        mLocateShopFAB = findViewById(R.id.fab_locate_others_shop);
        mLocateShopFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ShopActivity.this, LocateSelectedShopMapsActivity.class);
                intent.putExtra("shop_location_lat", mShopGeopoint.getLatitude());
                intent.putExtra("shop_location_long", mShopGeopoint.getLongitude());
                intent.putExtra("shop_name", shopName);
                startActivity(intent);
            }
        });
    }
}