package com.example.mentalhealth;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.navigation.NavigationBarView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link moodmenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class moodmenu extends Fragment  {


Spinner moodselect;

Button moodtoshow;

String mood;
    public moodmenu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment moodmenu.
     */
    // TODO: Rename and change types and number of parameters




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview= inflater.inflate(R.layout.fragment_moodmenu, container, false);

        moodselect = (Spinner) rootview.findViewById(R.id.spinner);

        moodtoshow = (Button) rootview.findViewById(R.id.button4);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.moodtype,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        moodselect.setAdapter(adapter);

        moodselect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mood = moodselect.getSelectedItem().toString();
                Log.d(TAG,"mood is: "+mood);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        moodtoshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectLocation selectLocation = new SelectLocation();
                Bundle args = new Bundle();
                args.putString("Mood",mood);
                selectLocation.setArguments(args);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flFragment,selectLocation)
                        .addToBackStack(null)
                        .commit();
            }
        });

















        return rootview;
    }



}