package com.example.mentalhealth;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FourthFragment newInstance} factory method to
 * create an instance of this fragment.
 */
public class FourthFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth;

    ImageButton pickdate;

    EditText setdate;

    EditText Title;

    EditText Date;


    EditText Description;




    Button submit;


    public FourthFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fourth,container,false); //inflate layout use instance of class View
        Bundle present = getArguments(); //get arguments from bundle to check if another page has sent arguments.
         Title = rootView.findViewById(R.id.Title);
         Date = rootView.findViewById(R.id.date);       //set fields up
         Description= rootView.findViewById(R.id.Description);

        Toolbar diarynav = (Toolbar) rootView.findViewById(R.id.toolbar); //toolbar instance of Toolbar class

        submit = (Button) rootView.findViewById(R.id.button);

        diarynav.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {                 //acessing menu items when clicked on.

                onNavigationItemSelected(menuItem);
                return false;
            }
        });



        diarynav.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Selectentry selectentry= new Selectentry();
                getActivity().getSupportFragmentManager().beginTransaction()    //loads activity when icon is pressed.
                        .replace(R.id.flFragment,selectentry)
                        .addToBackStack(null)
                        .commit();

            }
        });

        setdate = (EditText) rootView.findViewById(R.id.date);
        MaterialDatePicker.Builder MaterialDateBuilder = MaterialDatePicker.Builder.datePicker();

        MaterialDateBuilder.setTitleText("Select date");

        final MaterialDatePicker materialDatePicker = MaterialDateBuilder.build();



        setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");//shows calendar fragment

            }


        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                setdate.setText(materialDatePicker.getHeaderText());//sets date edit text as date from fragment.
            }
        });





        if(present != null){
            settexts(Title,Date,Description); //check bundle is not empty to know that the edit bundle has been sent.
        }








        submit.setOnClickListener(new View.OnClickListener() {


            public void onClick (View v){




                String title = Title.getText().toString();
                String date = Date.getText().toString();
                String description = Description.getText().toString();

                Log.d(TAG,"Title is "+title);

                if ( "".equals(title)|| "".equals(date) || "".equals(description)){//checks all fields are filled
                    Toast.makeText(getContext(),
                            "Please fill all fields !",
                            Toast.LENGTH_SHORT).show();


                }

                else{
                    senddata(title,description,date); //if fields are all filled submit data to be stored in database
                }





            }
        });




        return rootView;
        //return inflater.inflate(R.layout.fragment_fourth, container, false);
    }

    public void settexts(EditText Title,EditText Date, EditText Description){

        String title = getArguments().getString("Title");
        String date = getArguments().getString("Date");
        String description = getArguments().getString("Description");  //used for incoming arguments from bundles.

        Title.setText(title);

        Date.setText(date);

        Description.setText(description);

    }

    public void senddata(String Title, String Description, String Date){
        mAuth = FirebaseAuth.getInstance();

        String currentUser = mAuth.getCurrentUser().getEmail().toString();  //Saving values in current users collection.

        Map<String, Object> user = new HashMap<>();
        user.put("Title", Title);               //putting data in database
        user.put("Date", Date);
        user.put("Description", Description);
// Add a new document with a generated ID
        db.collection(currentUser).document(Title)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void avoid) {
                        Log.d("MainActivity", "DocumentSnapshot added with ID: ");
                        Toast.makeText(getContext(), "Diary entry added",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MainActivity", "Error adding document", e);
                    }
                });



    }

    public boolean
    onNavigationItemSelected(@androidx.annotation.NonNull MenuItem item){

        int itemId = item.getItemId();

        {
            if(itemId == R.id.clearDescription){  //if id in overflow menu touched edittexts turn to null.

                Description.setText(null);



                return true;
            }

            else if(itemId == R.id.cleardate){
                Date.setText(null);
            }

            else if (itemId == R.id.cleartitle){
                Title.setText(null);
            }


        }

        return false;

    }
}