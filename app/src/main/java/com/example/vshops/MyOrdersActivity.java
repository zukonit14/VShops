package com.example.vshops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.vshops.AdapterClass.OrderAdapter;
import com.example.vshops.ModelClass.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyOrdersActivity extends AppCompatActivity {

    ListView mMyOrdersListView;
    OrderAdapter mMyOrdersAdapter;
    private ArrayList<Order>arrayOfOrders;

    private FirebaseFirestore mFirebaseFirestore=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        //Inserting Dummy data for testing purposes
         arrayOfOrders=new ArrayList<Order>();

         mFirebaseFirestore.collection("orders")
                 .whereEqualTo("shop_email", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                 .whereEqualTo("status","Pending")
                 .get()
                 .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<QuerySnapshot> task) {

                         if(task.isSuccessful()){
                             for(QueryDocumentSnapshot doc:task.getResult()){

                                 Order order=new Order();
                                 order.mTimestamp=(Timestamp) doc.get("time");
                                 order.mStatus=doc.get("status").toString();
                                 order.mCustomerEmail=doc.get("customer_email").toString();
                                 order.mShopKeeperEmail=doc.get("shop_email").toString();
                                 order.mProductID=doc.get("product_id").toString();
                                 order.mQty=doc.get("qty").toString();
                                 order.mOrderID=doc.getId();
                                 order.mProductName=doc.get("product_name").toString();
                                 arrayOfOrders.add(order);
                             }
                             // Create the adapter to convert the array to views
                             mMyOrdersAdapter=new OrderAdapter(MyOrdersActivity.this,arrayOfOrders,1);

                             // Attach the adapter to a ListView
                             mMyOrdersListView=findViewById(R.id.my_order_list_view);
                             mMyOrdersListView.setAdapter(mMyOrdersAdapter);
                         }
                     }
                 });



    }
}