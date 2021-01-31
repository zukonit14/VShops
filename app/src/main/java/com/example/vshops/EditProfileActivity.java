package com.example.vshops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vshops.Fragments.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private EditText mName,mMobile,mAddress,mShopName;
    private TextView mLocation;
    private Button mSaveButton;
    private FloatingActionButton mEditShopLocationFAB;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore mFirebaseFirestore=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);



        mName=findViewById(R.id.edit_user_name_edit_text);
        mMobile=findViewById(R.id.edit_user_mobile_edit_text);
        mAddress=findViewById(R.id.edit_user_address_edit_text);
        mShopName=findViewById(R.id.edit_user_shop_name_edit_text);
        mLocation=findViewById(R.id.edit_user_location_set_text_view);
        mSaveButton=findViewById(R.id.edit_user_save_button);

        fetchValues();



        mEditShopLocationFAB=findViewById(R.id.fab_edit_shop_location);
        mEditShopLocationFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EditProfileActivity.this,LocateShopMapsActivity.class));

            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,Object> user=new HashMap<>();
                user.put("name",mName.getText().toString());
                user.put("mobile",mMobile.getText().toString());
                user.put("address",mAddress.getText().toString());
                if(mShopName.getText().toString().trim().isEmpty())
                {
                    user.put("shop_name","NA");
                }
                else
                    user.put("shop_name",mShopName.getText().toString());


                mFirebaseFirestore.collection("users")
                        .document(mAuth.getCurrentUser().getEmail())
                        .set(user, SetOptions.merge());


                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);



            }
        });
    }

    private void fetchValues()
    {
        mFirebaseFirestore.collection("users")
                .whereEqualTo(FieldPath.documentId(),mAuth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult()){

                                mName.setText(documentSnapshot.get("name").toString());
                                mMobile.setText(documentSnapshot.get("mobile").toString());
                                mAddress.setText(documentSnapshot.get("address").toString());
                                mShopName.setText(documentSnapshot.get("shop_name").toString());
                                GeoPoint geoPoint=(GeoPoint)documentSnapshot.get("shop_location");
                                if(geoPoint.getLongitude()==0&&geoPoint.getLatitude()==0)
                                {
                                    mLocation.setText("Location : NOT SET");
                                }
                                else mLocation.setText("Location : SET");
                            }
                        }
                    }
                });
    }
}