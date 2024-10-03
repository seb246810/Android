package com.example.mentalhealth;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.core.widget.ListViewCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Selectentry extends Fragment {

    Button  CreateEntry;

    FourthFragment fourthFragment= new FourthFragment();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    Toolbar toolbar;

    // TODO: Rename parameter arguments, choose names that match


    public Selectentry() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_selectentry, container, false);



        toolbar = rootView.findViewById(R.id.toolbar);



        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                onNavigationItemSelected(menuItem);
                return false;
            }
        });






        ListView listView = rootView.findViewById(R.id.examplelistview);


        mAuth = FirebaseAuth.getInstance();

        String currentUser = mAuth.getCurrentUser().getEmail().toString();
        List<infoModel> infoModelList = new ArrayList<>();

        db.collection(currentUser).orderBy("Title")//get all entries that contain title

                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG,document.getId() + " => " + document.getData());

                                infoModelList.add(new infoModel(document.getString("Title"), document.getString("Date"),""));
                                //store pulled field data into infomodel




                            }

                            infoAdapter infoAdapter = new infoAdapter(getContext(), infoModelList);

                            listView.setAdapter(infoAdapter);//set title and description in list view.

                            if(infoModelList.isEmpty()){
                                Toast.makeText(getContext(),
                                        "List is empty create an entry with the plus icon !",
                                        Toast.LENGTH_SHORT).show();
                            }


                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapter, View view, int position, long l) {

                                    infoModel item = (infoModel) adapter.getItemAtPosition(position); //instancr of infomodel to get specifc entry.

                                    String title= item.gettitle();//store title in string



                                    Log.d(TAG,"values "+title);


                                    db.collection(currentUser)
                                            .whereEqualTo(FieldPath.documentId(), title)//pull fields that have the fieldpathID that correspond to the title.
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                                            String Title=document.getString("Title"); //store values for bundle
                                                            String Date = document.getString("Date");
                                                            String Description = document.getString("Description");

                                                            showdiaryentry show = new showdiaryentry();

                                                            Bundle args = new Bundle();

                                                            args.putString("Title",Title);

                                                            args.putString("Date",Date);

                                                            args.putString("Description",Description); //setting arguments

                                                            show.setArguments(args);


                                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                                    .replace(R.id.flFragment,show)
                                                                    .addToBackStack(null) //move to new fragment with bundle.
                                                                    .commit();

                                                        }
                                                    } else {
                                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });

                                }
                            });




                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });










        return rootView;
    }


    public boolean
    onNavigationItemSelected(@androidx.annotation.NonNull MenuItem item){

        int itemId = item.getItemId();

        {
            if(itemId == R.id.entry){
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment,fourthFragment)
                        .commit();
                return true;
            }


        }

        return false;

    }


}