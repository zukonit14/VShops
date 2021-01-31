package com.example.vshops.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;



import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vshops.EditProfileActivity;
import com.example.vshops.LoginActivity;
import com.example.vshops.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;


public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore mFirebaseFirestore=FirebaseFirestore.getInstance();
    private TextView mName,mMobile,mAddress,mShopName,mShopLocation,mEmail;

    //Enable Menu Options in this fragment
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //Inflate menu
        inflater.inflate(R.menu.profile_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Handling menu item clicks
        switch (item.getItemId())
        {
            case R.id.edit_profile_item:
            {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
                break;
            }
            case R.id.share_profile_item:
            {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody="Visit My Online Shop on VShops App and Enjoy Shopping Online with local Shops.\nFollow this link to visit the My Shop : \n";
                shareBody+="http://www.vshops.com/" + mEmail.getText().toString();
                intent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(intent,"Share using"));
                break;
            }
            case R.id.logout_profile_item:
            {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);



        mName=view.findViewById(R.id.profile_name_text_view);
        mEmail=view.findViewById(R.id.profile_email_text_view);
        mMobile=view.findViewById(R.id.profile_mobile_text_view);
        mAddress=view.findViewById(R.id.profile_address_text_view);
        mShopName=view.findViewById(R.id.profile_shop_name_text_view);
        mShopLocation=view.findViewById(R.id.profile_shop_location_text_view);

        mFirebaseFirestore.collection("users")
                .document(mAuth.getCurrentUser().getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){

                            mName.setText(documentSnapshot.get("name").toString());
                            mEmail.setText(documentSnapshot.getId());
                            mAddress.setText(documentSnapshot.get("address").toString());
                            mMobile.setText(documentSnapshot.get("mobile").toString());
                            mShopName.setText(documentSnapshot.get("shop_name").toString());
                            GeoPoint geoPoint=new GeoPoint(0,0);
                            if(documentSnapshot.get("shop_location").equals(geoPoint)==false){
                                mShopLocation.setText("Shop Location: SET");
                           }
                            else
                           {
                                mShopLocation.setText("Shop Location: NOT SET");
                            }

                        }

                    }
                });

        return view;
    }
}