package com.example.mentalhealth;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {
    Button signout;

    TextView email;

    private FirebaseAuth mAuth;




    public FirstFragment() {
        // Required empty public constructor
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootview= inflater.inflate(R.layout.fragment_first, container, false);

        signout = (Button) rootview.findViewById(R.id.Button1);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Toast toast = Toast.makeText(getContext(), "Signed out ", Toast.LENGTH_LONG );
                toast.show();

                Intent intent =new Intent(getActivity(),ExistingUser.class);
                startActivity(intent);



            }
        });

        mAuth = FirebaseAuth.getInstance();

        email = (TextView) rootview.findViewById(R.id.Textview);

        String Email = mAuth.getCurrentUser().getEmail().toString();

        email.setText(Email);


        return rootview;
    }
}