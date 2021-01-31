package com.example.vshops;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class OrderProductActivity extends AppCompatActivity {

    private TextView mQtyTextView,mProductTitleTextView,mProductDetailsTextView,mProductPriceTextView;
    private ImageView mProductImageView;
    private  Button mAddButton,mSubtractButton,mOrderButton;

    private static final int PICK_IMAGE_REQUEST=1;
    private StorageReference mStorageReference;
    private FirebaseFirestore mFirebaseFirestore=FirebaseFirestore.getInstance();
    private StorageTask mUploadTask;
    private Uri mImageUri;
    private String mProductID,mShopEmailID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product);

        mProductID=getIntent().getStringExtra("productID");
        mShopEmailID=getIntent().getStringExtra("shopEmailID");


        mQtyTextView=findViewById(R.id.qty_text_view);
        mProductTitleTextView=findViewById(R.id.product_title_text_view);
        mProductDetailsTextView=findViewById(R.id.product_details_text_view);
        mProductPriceTextView=findViewById(R.id.product_price_text_view);

        mProductImageView=findViewById(R.id.product_image_view);

        mAddButton=findViewById(R.id.plus_button);
        mSubtractButton=findViewById(R.id.minus_button);
        mOrderButton=findViewById(R.id.product_order_button);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number=mQtyTextView.getText().toString();
                int num=Integer.parseInt(number);
                num++;
                if(num>10) num=10;
                number = Integer.toString(num);
                mQtyTextView.setText(number);
            }
        });

        mSubtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number=mQtyTextView.getText().toString();
                int num=Integer.parseInt(number);
                num--;
                if(num<1) num=1;
                number = Integer.toString(num);
                mQtyTextView.setText(number);
            }
        });


        mFirebaseFirestore.collection("users")
                .document(mShopEmailID)
                .collection("products")
                .document(mProductID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    mProductTitleTextView.setText(documentSnapshot.get("product_name").toString());
                    mProductDetailsTextView.setText(documentSnapshot.get("details").toString());
                    mProductPriceTextView.setText("₹"+documentSnapshot.get("price").toString());

                    mStorageReference= FirebaseStorage.getInstance().getReference("uploads")
                            .child(documentSnapshot.get("image").toString());

                    mStorageReference.getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUri=uri.toString();
                                    Glide.with(getApplicationContext())
                                            .load(imageUri)
                                            .into(mProductImageView);
                                }
                            });


                }
            }
        });

        mOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderProduct();
            }
        });

    }

    private void orderProduct() {
            Map<String,Object> order=new HashMap<>();
            order.put("product_id",mProductID);
            order.put("shop_email",mShopEmailID);
            order.put("customer_email",FirebaseAuth.getInstance().getCurrentUser().getEmail());
            order.put("qty",mQtyTextView.getText().toString());
            order.put("status","Pending");
            order.put("product_name",mProductTitleTextView.getText().toString());
            Timestamp timestamp=Timestamp.now();
            order.put("time",timestamp);


            mFirebaseFirestore.collection("orders")
                    .document()
                    .set(order)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(OrderProductActivity.this, "Order Placed successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(OrderProductActivity.this,MyPurchasesActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OrderProductActivity.this, "Order placing failed", Toast.LENGTH_SHORT).show();
                        }
                    });
    }


}