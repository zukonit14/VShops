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
import com.example.vshops.ModelClass.Shop;
import com.example.vshops.R;
import com.example.vshops.ShopActivity;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;

public class ShopAdapter extends ArrayAdapter<Shop> {

    private ArrayList<Shop> mShopArrayList;
    public ShopAdapter(Context context, ArrayList<Shop> shops) {
        super(context, 0, shops);
        mShopArrayList=shops;
    }

    @Override
    public int getCount() {
        return mShopArrayList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        //Shop shop =(Shop) mShopArrayList.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_shop, parent, false);
        }
        // Lookup view for data population
        TextView shopName = (TextView) convertView.findViewById(R.id.shop_name);
        TextView shopEmailID=(TextView)convertView.findViewById(R.id.shop_email_id);

        // Populate the data into the template view using the data object
        shopName.setText(mShopArrayList.get(position).mShopName);
        shopEmailID.setText(mShopArrayList.get(position).mEmailID);
        // Return the completed view to render on screen
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=shopName.getText().toString();
                Intent intent=new Intent(getContext(), ShopActivity.class);
                intent.putExtra("shopEmailID",mShopArrayList.get(position).mEmailID);
               getContext().startActivity(intent);
            }
        });
        return convertView;
    }

    public void updateDataset(ArrayList<Shop>shopArrayList)
    {
            mShopArrayList=new ArrayList<Shop>();
            mShopArrayList.addAll(shopArrayList);
            notifyDataSetChanged();//to refresh ListView with current arraylist
    }
}
