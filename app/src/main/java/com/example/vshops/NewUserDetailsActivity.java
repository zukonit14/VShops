package com.example.vshops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

public class NewUserDetailsActivity extends AppCompatActivity {

    private String EmailID,pwd;
    private EditText mUserNameEditText,mMobileEditText,mAddressEditText,mShopNameEditText;
    private Button mDoneButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirebaseFirestore=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_details);

        mUserNameEditText=findViewById(R.id.user_name_edit_text);
        mMobileEditText=findViewById(R.id.mobile_edit_text);
        mAddressEditText=findViewById(R.id.address_edit_text);
        mShopNameEditText=findViewById(R.id.shop_name_edit_text);
        mDoneButton=findViewById(R.id.done_button);

        mAuth=FirebaseAuth.getInstance();


        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUserNameEditText.getText().toString().trim().isEmpty()||mMobileEditText.getText().toString().trim().isEmpty()||mAddressEditText.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(NewUserDetailsActivity.this, "One or more empty fields", Toast.LENGTH_SHORT).show();
                }
                else if(validateMobile(mMobileEditText.getText().toString()))
                {
                    Toast.makeText(NewUserDetailsActivity.this, "Enter valid mobile number.", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    getDetails();
                }
            }
        });

        EmailID=getIntent().getStringExtra("EmailID");
        pwd=getIntent().getStringExtra("password");



    }

    private boolean validateMobile(String toString) {
        if(toString.length()!=10)return true;
        int i,n=toString.length();
        for(i=0;i<n;i++)
        {
            if(toString.charAt(i)>'9'||toString.charAt(i)<'0')return true;
        }
        return false;

    }

    boolean getDetails()
    {
        Map<String,Object> user=new HashMap<>();
        user.put("name",mUserNameEditText.getText().toString());
        user.put("mobile",mMobileEditText.getText().toString());
        user.put("address",mAddressEditText.getText().toString());
        user.put("password",pwd);
        if(mShopNameEditText.getText().toString().trim().isEmpty())
        {
            user.put("shop_name","NA");
        }
        else
        user.put("shop_name",mShopNameEditText.getText().toString());

        GeoPoint geoPoint=new GeoPoint(0,0);
        user.put("shop_location",geoPoint);

        mFirebaseFirestore.collection("users")
                .document(EmailID)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        registerUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewUserDetailsActivity.this,"User register failure",Toast.LENGTH_SHORT).show();
                    }
                });

        return  true;
    }

    void registerUser(){
        mAuth.createUserWithEmailAndPassword(EmailID,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {

                    Toast.makeText(NewUserDetailsActivity.this,"User registered successfully",Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(NewUserDetailsActivity.this,MainActivity.class));
                    finish();
                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(NewUserDetailsActivity.this,"User is already registered",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }
}