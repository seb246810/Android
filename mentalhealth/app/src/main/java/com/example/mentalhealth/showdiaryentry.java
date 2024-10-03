package com.example.mentalhealth;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link showdiaryentry#newInstance} factory method to
 * create an instance of this fragment.
 */
public class showdiaryentry extends Fragment {

Button delete;

Button editentry;

String Title;

String Date;

String Description;

FirebaseFirestore db = FirebaseFirestore.getInstance();

private FirebaseAuth mAuth;

    public showdiaryentry() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_showdiaryentry, container, false);



        TextView Showentry= (TextView)rootView.findViewById(R.id.textView2);

        Toolbar diarynav = (Toolbar) rootView.findViewById(R.id.toolbar);





        diarynav.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                                                                                    //set listener for item navigation.
                onNavigationItemSelected(menuItem);
                return false;
            }
        });

        diarynav.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Selectentry selectentry= new Selectentry();
                getActivity().getSupportFragmentManager().beginTransaction()//set back arrow listener to go back to other fragment.
                        .replace(R.id.flFragment,selectentry)
                        .addToBackStack(null)
                        .commit();

            }
        });
        // Inflate the layout for this fragmen
        Title = getArguments().getString("Title");
        Date = getArguments().getString("Date");
        Description = getArguments().getString("Description"); //get argumnets from bundle




        Showentry.setText(Date+"\n"+Title+"\n"+Description+"\n"); //set text with bundle argumnets.


        Log.d(TAG,"The value is "+Title +", "+ Date+", " + Description);









        return rootView;

    }


    public boolean
    onNavigationItemSelected(@androidx.annotation.NonNull MenuItem item){

        int itemId = item.getItemId();

        {
            if(itemId == R.id.delete){
                mAuth = FirebaseAuth.getInstance();

                String currentuser= mAuth.getCurrentUser().getEmail().toString();

                db.collection(currentuser).document(Title)//delete by id whihc is the title from current user collection.
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                Toast.makeText(getContext(),"Successfully deleted", Toast.LENGTH_SHORT).show();

                                Selectentry selectentry = new Selectentry();
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .addToBackStack(null)
                                        .replace(R.id.flFragment,selectentry)
                                        .commit();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });

                return true;
            } else if (itemId==R.id.edit) {

                FourthFragment edit = new FourthFragment();

                Bundle args = new Bundle();

                args.putString("Title",Title);

                args.putString("Date",Date);

                args.putString("Description",Description); //create bundle

                edit.setArguments(args);


                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flFragment,edit)
                        .addToBackStack(null)
                        .commit();                          //send to new fragment.



            }


        }

        return false;

    }


    public void setDiary(String title,String Date,String Description){

    }
}