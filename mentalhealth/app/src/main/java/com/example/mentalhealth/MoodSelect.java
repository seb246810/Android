package com.example.mentalhealth;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoodSelect#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoodSelect extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth;

    Button storemood;



    String moods;

    Button showlocation;

    TextView feelings;


    LocationTracking locationTrack;



    // TODO: Rename parameter arguments, choose names that match


    public MoodSelect() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoodSelect.
     */
    // TODO: Rename and change types and number of parameters




    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootview=inflater.inflate(R.layout.fragment_mood_select, container, false);

        Toolbar toolbar = (Toolbar) rootview.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectLocation selectlocation= new SelectLocation();
                getActivity().getSupportFragmentManager().beginTransaction() //navigation listener goes back to location screen when clicked
                        .replace(R.id.flFragment,selectlocation)
                        .addToBackStack(null)
                        .commit();

            }
        });





        storemood=(Button)rootview.findViewById(R.id.button2);

        Slider slider = (Slider)rootview.findViewById(R.id.continuousRangeSlider);




        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                //Use the value

                Integer slide_value = Math.round(value); //round value
                Log.d(TAG,"value is" +slide_value);
                Mood(slide_value);//pass value from slider to Mood



                feelings = (TextView) rootview.findViewById(R.id.editText2);



                feelings.setText("How are you feeling today ? "+"\n"+"You are feeling: "+moods); //uses global variable moods for slider value conversion.







                Log.d(TAG,"Mood = "+moods);





            }

        });

        Log.d(TAG,"Moods is "+moods);










        storemood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationTrack = new LocationTracking(getContext());
                if (locationTrack.canGetLocation()) {

                    double latitude = locationTrack.getLatitude();
                    double longitude = locationTrack.getLongitude();

                    storemood(moods,latitude,longitude); //pass locations and moods into storemood for database entry.


                } else {

                    locationTrack.showSettingsAlert();

                }

























            }
        });







        Log.d(TAG,"Moods is "+moods);





        // Inflate the layout for this fragment
        return rootview;
    }

    public void Mood(int rating) {
        String mood;




        if (rating <= 3) {
            mood = "Unhappy";


        } else if (rating >= 4 && rating <= 6) {
            mood = "Unsure";


        } else {
            mood = "Happy";


        }

        moods = mood;//sets global variable as the conversion







    }



    public void storemood (String mood,double latitude,double longitude){
        mAuth = FirebaseAuth.getInstance();


        String currentUser = mAuth.getCurrentUser().getEmail().toString();

        Log.d(TAG,mood+ latitude+ longitude);

        Random random = new Random();//random class

        String ID = UUID.randomUUID().toString();//create random unique id for the entry



        Map<String, Object> user = new HashMap<>();

        user.put("LocationID",ID);
        user.put("Mood", mood);
        user.put("latitude", latitude);
        user.put("longitude",longitude);//put values into fields.

        db.collection(currentUser).document(ID)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                  @Override
                                                  public void onSuccess(Void avoid) {
                                                      Log.d("MainActivity", "DocumentSnapshot added with ID: ");
                                                  }
                                              })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MainActivity", "Error adding document", e);
                    }
                });







        Intent intent = new Intent(getContext(),Viewlocation.class);

        Bundle args = new Bundle(); //bundle intent

        args.putString("mood",mood);

        args.putDouble("long",longitude); //takes passed values

        args.putDouble("lat",latitude);


        intent.putExtras(args);

        startActivity(intent);//moves to viewlocation







    }









}