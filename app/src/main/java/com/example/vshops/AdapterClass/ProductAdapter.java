package com.example.vshops.AdapterClass;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vshops.ModelClass.Product;
import com.example.vshops.MyProductViewActivity;
import com.example.vshops.OrderProductActivity;
import com.example.vshops.R;
import com.example.vshops.ShopActivity;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {

    private ArrayList<Product>mProductArrayList;
    private int flag;
    private static final int ORDER_PRODUCT=0;
    private static final int MY_PRODUCT_VIEW=1;
    public ProductAdapter(Context context, ArrayList<Product> users,int flag) {
        super(context, 0, users);
        mProductArrayList=users;
        this.flag=flag;
    }

    @Override
    public int getCount() {
        return mProductArrayList.size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Product product = mProductArrayList.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_my_product, parent, false);
        }
        // Lookup view for data population
        TextView productName = (TextView) convertView.findViewById(R.id.my_product_name);

        // Populate the data into the template view using the data object
        productName.setText(product.mProductName);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=productName.getText().toString();

                if(flag==ORDER_PRODUCT){

                    Intent intent=new Intent(getContext(), OrderProductActivity.class);
                    intent.putExtra("shopEmailID",mProductArrayList.get(position).mShopemailID);
                    intent.putExtra("productID",mProductArrayList.get(position).mProductID);
                    getContext().startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(getContext(), MyProductViewActivity.class);
                    intent.putExtra("productID",mProductArrayList.get(position).mProductID);
                    getContext().startActivity(intent);
                }

                //getContext().startActivity(intent);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
