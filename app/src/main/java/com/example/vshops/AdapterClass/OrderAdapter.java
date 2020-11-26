package com.example.vshops.AdapterClass;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vshops.ModelClass.Order;
import com.example.vshops.ModelClass.Shop;
import com.example.vshops.OrderDescriptionActivity;
import com.example.vshops.R;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends ArrayAdapter<Order> {

    private ArrayList<Order> mOrdersArrayList;
    private int flag;
    public static final int  MY_ORDERS=1;
    public OrderAdapter(Context context, ArrayList<Order> orders,int flag) {
        super(context, 0, orders);
        mOrdersArrayList =orders;
        this.flag=flag;
    }

    @Override
    public int getCount() {
        return mOrdersArrayList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Order order = mOrdersArrayList.get(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_order, parent, false);
        }

        // Lookup view for data population
        TextView productName = (TextView) convertView.findViewById(R.id.order_product_name_text_view);
        TextView time=(TextView)convertView.findViewById(R.id.order_time_stamp_text_view);
        TextView status=(TextView)convertView.findViewById(R.id.order_status_text_view);
        TextView qty=(TextView)convertView.findViewById(R.id.order_qty_text_view);

        // Populate the data into the template view using the data object
        productName.setText(mOrdersArrayList.get(position).mProductName);
        status.setText(mOrdersArrayList.get(position).mStatus);
        time.setText(mOrdersArrayList.get(position).mTimestamp.toDate().toString());
        qty.setText(mOrdersArrayList.get(position).mQty);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag==MY_ORDERS){
                    Intent intent=new Intent(getContext(), OrderDescriptionActivity.class);
                    intent.putExtra("orderID",mOrdersArrayList.get(position).mOrderID);
                    getContext().startActivity(intent);

                }

            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
