package com.example.vshops;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vshops.AdapterClass.ShopAdapter;
import com.example.vshops.ModelClass.Shop;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocateAllShopsMapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowLongClickListener {

    private GoogleMap mMap;
    private ArrayList<Shop> mArrayOfShops;
    private Map<String, GeoPoint> geoPoint;
    private FirebaseFirestore mFirebaseFirestore=FirebaseFirestore.getInstance();

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_all_shops_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.all_shops_map);
        mapFragment.getMapAsync(this);

        geoPoint= new HashMap<>();
        mArrayOfShops=new ArrayList<Shop>();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();




    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;

                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.all_shops_map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(LocateAllShopsMapsActivity.this);
                }
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
                                    geoPoint.put(emailID,(GeoPoint) document.get("shop_location"));
                                }
                            }
                            for(Shop shop:mArrayOfShops){

                                LatLng Shop = new LatLng(geoPoint.get(shop.mEmailID).getLatitude(),geoPoint.get(shop.mEmailID).getLongitude());
                                MarkerOptions markerOptions=new MarkerOptions();
                                markerOptions.draggable(false).title(shop.mShopName).snippet(shop.mEmailID).position(Shop);
                                mMap.addMarker(markerOptions);


                            }
                            LatLng latLng;
                            latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            mMap.setOnInfoWindowLongClickListener(LocateAllShopsMapsActivity.this);
                        }
                    }
                });


        //LatLng latLng;
        /*if(currentLocation!=null) latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        else latLng = new LatLng(18,73);*/



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {

        startActivity(new Intent(LocateAllShopsMapsActivity.this,ShopActivity.class).putExtra("shopEmailID",marker.getSnippet()));
    }
}