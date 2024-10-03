package com.example.mentalhealth;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectLocation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectLocation extends Fragment {

    Spinner moodselect;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    MoodSelect moodSelect = new MoodSelect();

    String mood;

    Toolbar toolbar;

    ListView displayloc;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SelectLocation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectLocation.
     */
    // TODO: Rename and change types and number of parameters




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview= inflater.inflate(R.layout.fragment_select_location, container, false);

        toolbar = (Toolbar) rootview.findViewById(R.id.toolbar);

        displayloc= (ListView) rootview.findViewById(R.id.examplelistview);

        moodselect = (Spinner) rootview.findViewById(R.id.spinner);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onNavigationItemSelected(item);
                return false;
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.moodtype,
                android.R.layout.simple_spinner_item
        );                                              //set up spinner to hold array.

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        moodselect.setAdapter(adapter);

        moodselect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mood = moodselect.getSelectedItem().toString(); //get selected item in spinner.
                Log.d(TAG,"mood is: "+mood);

                loadmood(); //load corresponding entries

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });






                            displayloc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    mAuth = FirebaseAuth.getInstance();

                                    String currentUser = mAuth.getCurrentUser().getEmail().toString();
                                    infoModel locationitem = (infoModel) adapterView.getItemAtPosition(i);

                                    String location=locationitem.getID();//get locationID stored in infomodel class.

                                    Log.d(TAG,location);

                                    db.collection(currentUser)
                                            .whereEqualTo(FieldPath.documentId(),location )//pull documentid that is equal to locationid
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@org.checkerframework.checker.nullness.qual.NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            Log.d(TAG, document.getId() + " => " + document.getData());

                                                            String mood = document.getString("Mood");
                                                            double latitude=document.getDouble("latitude");
                                                            double longitude = document.getDouble("longitude");


                                                            Intent intent = new Intent(getContext(),Viewlocation.class);

                                                            Bundle args = new Bundle();

                                                            args.putString("mood",mood);

                                                            args.putDouble("long",longitude);

                                                            args.putDouble("lat",latitude);     //Bundle the fields that correspond with the id.


                                                            intent.putExtras(args);

                                                            startActivity(intent);

                                                        }
                                                    } else {
                                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });

                                }
                            });








        return rootview;
    }

    public void loadmood() {
        mAuth = FirebaseAuth.getInstance();

        String currentUser = mAuth.getCurrentUser().getEmail().toString();
        List<infoModel> infoModelList = new ArrayList<>();

        db.collection(currentUser)
                .whereEqualTo("Mood", mood)         //pull documents that have a field location id and field mood is equal to spinner mood.
                .orderBy("LocationID")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                infoModelList.add(new infoModel(document.getString("Mood"), "Your location for where you felt " + document.getString("Mood"), document.getString("LocationID")));



                            }

                            infoAdapter infoAdapter = new infoAdapter(getContext(), infoModelList);

                            displayloc.setAdapter(infoAdapter);//set infomodel values into list view.




                        }
                    }


                });
    }


    public boolean
    onNavigationItemSelected(@androidx.annotation.NonNull MenuItem item){

        int itemId = item.getItemId();

        {
            if(itemId == R.id.entry){
                getActivity().getSupportFragmentManager()           //when icon is clicked load moodselect fragment.
                        .beginTransaction()
                        .replace(R.id.flFragment,moodSelect)
                        .commit();
                return true;
            }


        }

        return false;

    }
}