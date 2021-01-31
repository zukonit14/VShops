package com.example.vshops.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vshops.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {

    private TextView textView,textViewName;
    private ImageView imageView;
    private FirebaseFirestore mFirebaseFireStore=FirebaseFirestore.getInstance();
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        textView=view.findViewById(R.id.welcome_text_view);
        textViewName=view.findViewById(R.id.name_text_view);


       String s= FirebaseAuth.getInstance().getCurrentUser().getEmail();
       textView.setText("Welcome");
       ShowGif(view);

       mFirebaseFireStore.collection("users")
               .document(s)
               .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
           @Override
           public void onSuccess(DocumentSnapshot documentSnapshot) {
               if(documentSnapshot.exists()){
                   textViewName.setText(documentSnapshot.get("name").toString());

               }
           }
       });
        return view;
    }
    public void ShowGif(View view) {
        ImageView imageView = view.findViewById(R.id.home_image_view);
        Glide.with(this).load(R.drawable.image_home).into(imageView);
    }
}