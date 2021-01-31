package com.example.vshops;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class OrderDescriptionActivity extends AppCompatActivity {

    private TextView mOrderIDTextView, mCustomerEmailTextView, mCustomerNameTextView, mCustomerAddressTextView, mCustomerMobileTextView, mProductNameTextView, mProductPriceTextView, mQuantityTextView;
    private Button mAcceptButton, mRejectButton;

    private FirebaseFirestore mFirebaseFireStore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String mOrderID, mCustomerEmail, mProductID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_description);
        mOrderIDTextView = findViewById(R.id.order_id_description_text_view);
        mCustomerEmailTextView = findViewById(R.id.customer_email_text_view);
        mCustomerNameTextView = findViewById(R.id.customer_name_text_view);
        mCustomerAddressTextView = findViewById(R.id.customer_address_text_view);
        mCustomerMobileTextView = findViewById(R.id.customer_mobile_text_view);
        mProductNameTextView = findViewById(R.id.product_name_order_description_text_view);
        mProductPriceTextView = findViewById(R.id.product_price_order_description_text_view);
        mQuantityTextView = findViewById(R.id.quantity_order_description_text_view);

        mAcceptButton = findViewById(R.id.order_description_accept_button);
        mRejectButton = findViewById(R.id.order_description_reject_button);

        mOrderID = getIntent().getStringExtra("orderID");
        mFirebaseFireStore.collection("orders")
                .document(mOrderID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {
                            mOrderIDTextView.setText("OrderID : " + mOrderID);
                            mCustomerEmailTextView.setText("Customer Email : " + documentSnapshot.get("customer_email").toString());
                            mCustomerEmail = documentSnapshot.get("customer_email").toString();
                            mQuantityTextView.setText("Quantity : " + documentSnapshot.get("qty").toString());
                            mProductNameTextView.setText("Product Name : " + documentSnapshot.get("product_name").toString());
                            mProductID = documentSnapshot.get("product_id").toString();


                            mFirebaseFireStore.collection("users")
                                    .document(mCustomerEmail)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                            if (documentSnapshot.exists()) {
                                                mCustomerNameTextView.setText("Customer Name : " + documentSnapshot.get("name").toString());
                                                mCustomerAddressTextView.setText("Customer Address : " + documentSnapshot.get("address").toString());
                                                mCustomerMobileTextView.setText("Customer Mobile : " + documentSnapshot.get("mobile").toString());


                                            }
                                        }
                                    });


                            mFirebaseFireStore.collection("users")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                                    .collection("products")
                                    .document(mProductID)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                            if (documentSnapshot.exists()) {
                                                mProductPriceTextView.setText("Price : ₹" + documentSnapshot.get("price").toString());
                                            }

                                        }
                                    });

                        }
                    }
                });


        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order("Accepted");
            }
        });

        mRejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order("Rejected");
            }
        });
    }

    private void Order(String status) {
        Map<String, Object> updateStatus = new HashMap<>();
        updateStatus.put("status", status);

        mFirebaseFireStore.collection("orders")
                .document(mOrderID)
                .set(updateStatus, SetOptions.merge());

        Intent intent = new Intent(OrderDescriptionActivity.this, MyOrdersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
}