package com.example.vshops.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vshops.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {

    private TextView textView;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        textView=view.findViewById(R.id.welcome_text_view);

        String s= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        textView.setText("Welcome "+s);
        return view;
    }
}