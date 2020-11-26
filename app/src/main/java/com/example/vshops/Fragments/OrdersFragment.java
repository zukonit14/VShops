package com.example.vshops.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vshops.MyOrdersActivity;
import com.example.vshops.MyPurchasesActivity;
import com.example.vshops.R;

public class OrdersFragment extends Fragment {

    private TextView mMyPurchases,mMyOrders;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_orders, container, false);

        mMyPurchases=view.findViewById(R.id.my_purchase_text_view);
        mMyOrders=view.findViewById(R.id.my_order_text_view);

        mMyPurchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Opening My Purchases..",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getContext(), MyPurchasesActivity.class);
                startActivity(intent);
            }
        });

        mMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Opening My Orders..",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getContext(), MyOrdersActivity.class);
                startActivity(intent);
            }
        });
        return  view;
    }
}