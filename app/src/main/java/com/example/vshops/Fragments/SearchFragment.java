package com.example.vshops.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vshops.AdapterClass.ProductAdapter;
import com.example.vshops.AdapterClass.ShopAdapter;
import com.example.vshops.ModelClass.Product;
import com.example.vshops.ModelClass.Shop;
import com.example.vshops.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchFragment extends Fragment {


    private  ArrayList<Shop> mArrayOfShops;
    private ListView mListView;


    private FirebaseFirestore mFirebaseFirestore=FirebaseFirestore.getInstance();
    public SearchFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);//inflate search_menu xml and displays in app

        MenuItem searchBar=menu.findItem(R.id.shop_search_item);//creating an object of menu item
        SearchView searchView=(SearchView) searchBar.getActionView();//returns the object of class that is specified within the actionViewClass field
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //gets called for every new input string
            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Shop>newShops=new ArrayList<Shop>();

                for(Shop x:mArrayOfShops){
                    if(x.mShopName.contains(newText)){
                        newShops.add(x);
                    }
                }
                ShopAdapter adapter=(ShopAdapter) mListView.getAdapter();
                adapter.updateDataset(newShops);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search, container, false);



        // Construct the data source
        mArrayOfShops = new ArrayList<Shop>();

        //Fetching  all shops
         mFirebaseFirestore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()  {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            for(QueryDocumentSnapshot document : task.getResult()){

                                    String shopName= document.get("shop_name").toString();
                                    String emailID=document.getId();

                                 if(shopName.equals("NA")==false&&(!emailID.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))) {
                                     Shop shop = new Shop(shopName, emailID);
                                     mArrayOfShops.add(shop);
                                 }

                            }

                            // Create the adapter to convert the array to views
                            ShopAdapter shopAdapter = new ShopAdapter(getContext(), mArrayOfShops);
                            // Attach the adapter to a ListView
                            mListView = (ListView) view.findViewById(R.id.shop_list_view);
                            mListView.setAdapter(shopAdapter);

                        }
                        else
                        {
                            Log.e("SearchFragment","Failed task to fetch users");
                        }
                    }
                });


        //Dummy Code for testing
        /*int i;
        for(i=1;i<=15;i++){
           // String s="Shop";
           // String n=Integer.toString(i);
            //n+="km";
            //s=s+Integer.toString(i);
            Log.e("SearchFragment",s+" "+e);
            Shop shop=new Shop(s,e);
            mArrayOfShops.add(shop);
        }*/


        FloatingActionButton fabOpenMap=view.findViewById(R.id.fab_view_map);
        fabOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Opening MAP",Toast.LENGTH_SHORT).show();
            }
        });
        return  view;
    }


}

/**This method is not suitable with Search View
 * i.e write onClick in getView method of Adapter
 */
        /*
          mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Shop selectedShop=(Shop) parent.getItemAtPosition(position);
                Toast.makeText(getContext(),"Name: "+selectedShop.mShopName,Toast.LENGTH_SHORT).show();
            }
        });

        */



//It's rather redundant to use async task with firebase calls which are async by design.


    /*private class FetchInBackground extends AsyncTask<FirebaseFirestore,Integer,ArrayList<Shop>>{

        @Override
        protected ArrayList<Shop> doInBackground(FirebaseFirestore... firebaseFirestores) {

                ArrayList<Shop> a=new ArrayList<Shop>();
            firebaseFirestores[0].collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()  {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){

                                for(QueryDocumentSnapshot document : task.getResult()){

                                    String shopName= document.get("shop_name").toString();
                                    String emailID=document.getId();
                                    //Log.e("SearchFragment",shopName);




                                    Shop shop =new Shop(shopName,emailID);
                                    //mArrayOfShops.add(shop);
                                    a.add(shop);


                                }
                               // shopAdapter.notifyDataSetChanged();//to refresh ListView with current arraylist

                                //Log.e("SearchFragment",Integer.toString(mArrayOfShops.size()));
                                //shopAdapter.addAll(mArrayOfShops);


                            }
                            else
                            {
                                Log.e("SearchFragment","Failed task to fetch users");
                            }
                        }
                    });
            return a;
        }

        @Override
        protected void onPostExecute(ArrayList<Shop> shopArrayList) {
            mArrayOfShops=shopArrayList;
            super.onPostExecute(shopArrayList);
        }
    }*/