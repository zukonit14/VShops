package com.example.vshops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vshops.AdapterClass.ProductAdapter;
import com.example.vshops.ModelClass.Product;
import com.example.vshops.ModelClass.Shop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {

    ListView mListView;
    FloatingActionButton mLocateShopFAB;
    TextView mShopName;
    Shop mShop;
    String shopName;
    ProductAdapter productAdapter;
    FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        mShopName = findViewById(R.id.shop_name_others_text_view);


        String shopEmailID = getIntent().getStringExtra("shopEmailID");
        Toast.makeText(ShopActivity.this, shopEmailID, Toast.LENGTH_SHORT).show();


        mFirebaseFirestore.collection("users")
                .whereEqualTo(FieldPath.documentId(), shopEmailID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                shopName = document.get("shop_name").toString();
                                mShopName.setText(shopName);
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
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document:task.getResult()){

                        Product product=new Product();
                        product.mProductName=document.get("product_name").toString();
                        product.mProductPrice=document.get("price").toString();
                        product.mProductImage=document.get("image").toString();
                        product.mProductID=document.getId();
                        product.mProductDetails=document.get("details").toString();
                        product.mShopemailID=shopEmailID;
                        arrayOfProducts.add(product);
                    }
                }
                // Create the adapter to convert the array to views
                productAdapter = new ProductAdapter(ShopActivity.this, arrayOfProducts,0);
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




       /* mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product selectedProduct = (Product) parent.getItemAtPosition(position);
                Toast.makeText(ShopActivity.this, "Name: " + selectedProduct.mProductName, Toast.LENGTH_SHORT).show();
            }
        });*/

        mLocateShopFAB = findViewById(R.id.fab_locate_others_shop);
        mLocateShopFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShopActivity.this, "Locating shop....", Toast.LENGTH_SHORT).show();
            }
        });
    }
}