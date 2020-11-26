package com.example.vshops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.vshops.Fragments.HomeFragment;
import com.example.vshops.Fragments.MyProductFragment;
import com.example.vshops.Fragments.OrdersFragment;
import com.example.vshops.Fragments.ProfileFragment;
import com.example.vshops.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private String mCustomerEmailID;
    private BottomNavigationView mBottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mBottomNavigationView=findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment_container,new HomeFragment());
        fragmentTransaction.commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment=null;
            switch (item.getItemId()){
                case R.id.home_item:
                    selectedFragment=new HomeFragment();
                    break;
                case R.id.my_products_item:
                    selectedFragment=new MyProductFragment();
                    break;
                case  R.id.orders_item:
                    selectedFragment=new OrdersFragment();
                    break;
                case R.id.profile_item:
                    selectedFragment=new ProfileFragment();
                    break;
                case R.id.search_item:
                    selectedFragment=new SearchFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,selectedFragment).commit();
            return true;
        }
    };
}