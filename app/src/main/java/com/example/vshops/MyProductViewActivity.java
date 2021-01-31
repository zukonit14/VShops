package com.example.vshops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class MyProductViewActivity extends AppCompatActivity {

    private TextView mMyProductTitleTextView, mMyProductPriceTextView, mMyProductDetailsTextView;
    private ImageView mMyProductImageView;
    private Button mMyProductDeleteButton;


    private StorageReference mStorageReference;
    private FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
    private String mProductID,mImageUrlForDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product_view);

        mMyProductTitleTextView = findViewById(R.id.my_product_title_text_view);
        mMyProductPriceTextView = findViewById(R.id.my_product_price_text_view);
        mMyProductDetailsTextView = findViewById(R.id.my_product_details_text_view);

        mMyProductImageView = findViewById(R.id.my_product_image_view);

        mMyProductDeleteButton = findViewById(R.id.my_product_delete_button);

        mProductID = getIntent().getStringExtra("productID");

        mFirebaseFirestore.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .collection("products")
                .document(mProductID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    mMyProductTitleTextView.setText(documentSnapshot.get("product_name").toString());
                    mMyProductDetailsTextView.setText(documentSnapshot.get("details").toString());
                    mMyProductPriceTextView.setText("₹"+documentSnapshot.get("price").toString());

                    mImageUrlForDelete=documentSnapshot.get("image").toString();
                    mStorageReference = FirebaseStorage.getInstance().getReference("uploads")
                            .child(mImageUrlForDelete);

                    mStorageReference.getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUri = uri.toString();
                                    Glide.with(getApplicationContext())
                                            .load(imageUri)
                                            .into(mMyProductImageView);
                                }
                            });
                }
            }
        });

        mMyProductDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseFirestore.collection("users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                        .collection("products")
                        .document(mProductID)
                        .delete();

                StorageReference storageReference=FirebaseStorage.getInstance()
                        .getReference()
                        .child("uploads/"+mImageUrlForDelete);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


                Intent intent = new Intent(MyProductViewActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });


    }
}