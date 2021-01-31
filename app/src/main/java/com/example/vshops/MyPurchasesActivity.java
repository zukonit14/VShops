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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyPurchasesActivity extends AppCompatActivity {

    private ListView mMyPurchasesListView;
    private OrderAdapter mMyPurchasesAdapter;
    private ArrayList<Order>arrayOfOrders;

    private FirebaseFirestore mFirebaseFirestore=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        //Inserting Dummy data for testing purposes
       arrayOfOrders=new ArrayList<Order>();

      //  CollectionReference collectionReference=mFirebaseFirestore.collection("orders");
       // collectionReference.orderBy("time",Query.Direction.DESCENDING);
       mFirebaseFirestore.collection("orders")
               .whereEqualTo("customer_email", FirebaseAuth.getInstance().getCurrentUser().getEmail())
               .get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
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
                                //arrayOfOrders.sort(sortByDate);
                                Collections.sort(arrayOfOrders, new Comparator<Order>() {
                                    @Override
                                    public int compare(Order o1, Order o2) {
                                       // o1.mTimestamp.c
                                         if(o1.mTimestamp.compareTo(o2.mTimestamp)>0)return -1;
                                         else if(o1.mTimestamp.compareTo(o2.mTimestamp)<0) return 1;
                                         else return 0;
                                    }
                                });
                                //Collections.reverse(arrayOfOrders);
                                // Create the adapter to convert the array to views
                                mMyPurchasesAdapter=new OrderAdapter(MyPurchasesActivity.this,arrayOfOrders,0);

                                // Attach the adapter to a ListView
                                mMyPurchasesListView=findViewById(R.id.my_order_list_view);
                                mMyPurchasesListView.setAdapter(mMyPurchasesAdapter);

                            }
                   }
               });




    }

    public boolean sortByDate(Order a,Order b){
        return (a.mTimestamp.getSeconds()<b.mTimestamp.getSeconds());
    }
}