package com.example.mentalhealth;



import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SecondFragment extends Fragment {
    ImageButton diaryentry;

    ImageButton Moodselect;

    Button signout;

    TextView welcometext;



    private  FirebaseAuth mAuth;



    public SecondFragment() {
        // Required empty public constructor
    }









    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_second,container,false);

        welcometext = (TextView) rootView.findViewById(R.id.textView3);

        welcometext.setText("Welcome !" + "\n"+"This is an application made to help your mental health."+"\n"+
                "The application includes a diary feature where you can create diary entries and a moood tracker to help" +
                "you find where you're most happy or sad at."+"\n"+"Use this application to relax your mind and feel at peace.");












        return rootView;



        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_second, container, false);
    }




    public void SignoutClicked(View view) {

        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseAuth.getInstance().signOut();

    }
}