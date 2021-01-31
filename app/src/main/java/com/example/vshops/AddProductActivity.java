package com.example.vshops;

import androidx.appcompat.app.AppCompatActivity;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {

    private ImageView mProductImageView;
    private EditText mName,mPrice,mDetails;
    private Button mAdd;

    private static final int PICK_IMAGE_REQUEST=1;
    private StorageReference mStorageReference;
    private FirebaseFirestore mFirebaseFirestore=FirebaseFirestore.getInstance();
    private StorageTask mUploadTask;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        mProductImageView=findViewById(R.id.add_product_image_view);

        mName=findViewById(R.id.add_product_name_edit_text);
        mPrice=findViewById(R.id.add_product_price_edit_text);
        mDetails=findViewById(R.id.add_product_details_edit_text);

        mAdd=findViewById(R.id.add_product_button);


        mStorageReference= FirebaseStorage.getInstance().getReference("uploads");
        mProductImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((mUploadTask!=null)&&(mUploadTask.isInProgress()))
                {

                }
                else
                {
                    uploadFile();

                }
            }
        });

    }

    public String getExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {

        if(mImageUri!=null){
            final String mImageName=System.currentTimeMillis()+"."+getExtension(mImageUri);
            StorageReference fileReference=mStorageReference.child(mImageName);
            mUploadTask=fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Map<String,Object> product=new HashMap<>();
                                product.put("product_name",mName.getText().toString());
                                product.put("price",mPrice.getText().toString());
                                product.put("details",mDetails.getText().toString());
                                product.put("image",mImageName);
                                mFirebaseFirestore.collection("users")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                                        .collection("products")
                                        .document()
                                       .set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(AddProductActivity.this,"Product added sucessfully",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AddProductActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                });
                        }
                    });
        }

    }



    private void openFileChooser() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            //Picasso.with(this).load(mImageUri).into(mImageView);
            mProductImageView.setImageURI(mImageUri);
        }
    }
}