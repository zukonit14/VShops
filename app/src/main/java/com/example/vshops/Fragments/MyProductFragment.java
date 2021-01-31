package com.example.vshops.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.vshops.AdapterClass.ProductAdapter;
import com.example.vshops.AddProductActivity;
import com.example.vshops.ModelClass.Product;
import com.example.vshops.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyProductFragment extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
    private ArrayList<Product> arrayOfProducts;
    private ProductAdapter productAdapter;

    public MyProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_product, container, false);


        // Construct the data source
        arrayOfProducts = new ArrayList<Product>();

        mFirebaseFirestore.collection("users")
                .document(mAuth.getCurrentUser().getEmail())
                .collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Product product = new Product();
                                product.mShopemailID = mAuth.getCurrentUser().getEmail();
                                product.mProductDetails = doc.get("details").toString();
                                product.mProductPrice = doc.get("price").toString();
                                product.mProductImage = doc.get("image").toString();
                                product.mProductName = doc.get("product_name").toString();
                                product.mProductID = doc.getId();
                                arrayOfProducts.add(product);
                            }
                            // Create the adapter to convert the array to views
                            productAdapter = new ProductAdapter(getContext(), arrayOfProducts, 1);
                            // Attach the adapter to a ListView
                            ListView listView = (ListView) view.findViewById(R.id.my_product_list_view);
                            listView.setAdapter(productAdapter);
                        }
                    }
                });


        FloatingActionButton fabAddProduct = view.findViewById(R.id.fab_product_add);
        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFirebaseFirestore.collection("users")
                        .whereEqualTo(FieldPath.documentId(), FirebaseAuth.getInstance().getCurrentUser().getEmail())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {


                            for (QueryDocumentSnapshot document : task.getResult()) {

                                GeoPoint geoPoint = (GeoPoint) document.get("shop_location");
                                if (document.get("shop_name").equals("NA") || geoPoint == new GeoPoint(0, 0)) {
                                    Toast.makeText(getContext(), "Register a shop in your profile to add products", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent intent = new Intent(getContext(), AddProductActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                });


            }
        });
        return view;
    }
}
/**
 * My Notes-->
 * Remember that in Fragments
 * 1)Use getContext() instead of this
 * 2)Use view.findViewById()
 */