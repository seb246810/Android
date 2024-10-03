package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Report#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Report extends Fragment {

    PieChart pieChart;

    double Food = 0;
    double Entertainment = 0;
    double Appliances = 0;

    double Healthcare=0;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Report() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment report.
     */
    // TODO: Rename and change types and number of parameters
    public static Report newInstance(String param1, String param2) {
        Report fragment = new Report();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        getFood();

        getAppliances();

        getHealthcare();

        getEntertainment();

        pieChart= view.findViewById(R.id.Piechart);

        initPieChart();

        showPiechart();




        return view;
    }

    private void initPieChart(){
        //using percentage as values instead of amount
        pieChart.setUsePercentValues(true);

        //remove the description label on the lower left corner, default true if not set
        pieChart.getDescription().setEnabled(false);

        //enabling the user to rotate the chart, default true
        pieChart.setRotationEnabled(true);
        //adding friction when rotating the pie chart
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        //setting the first entry start from right hand side, default starting from top
        pieChart.setRotationAngle(0);

        //highlight the entry when it is tapped, default true if not set
        pieChart.setHighlightPerTapEnabled(true);
        //adding animation so the entries pop up from 0 degree
        pieChart.animateY(1400, Easing.EaseInOutQuad);
        //setting the color of the hole in the middle, default white
        pieChart.setHoleColor(Color.parseColor("#000000"));

    }

    private void showPiechart(){

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "type";

        Map<String, Double> typeAmountMap = new HashMap<>();
        typeAmountMap.put("Food",Food);
        typeAmountMap.put("Appliances",Appliances);
        typeAmountMap.put("Entertainment",Entertainment);
        typeAmountMap.put("Healthcare",Healthcare);


        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#304567"));
        colors.add(Color.parseColor("#309967"));
        colors.add(Color.parseColor("#476567"));
        colors.add(Color.parseColor("#890567"));
        colors.add(Color.parseColor("#a35567"));
        colors.add(Color.parseColor("#ff5f67"));
        colors.add(Color.parseColor("#3ca567"));

        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(),type));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);

        pieDataSet.setValueTextSize(12f);

        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);

        pieData.setDrawValues(true);

        pieChart.setData(pieData);

        pieChart.invalidate();




    }

    public void getFood(){
        db.collection("receipts")
                .whereEqualTo("Category","Food")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Double food=0.0;
                        for(QueryDocumentSnapshot document: task.getResult()){
                            String Price = document.getString("Total");
                            Log.d(TAG,"String is"+Price);
                            Double price = Double.parseDouble(Price);
                            Log.d(TAG,"double is"+price);

                            food += price;
                            Food = food;
                            Log.d(TAG,"Food is"+Food);
                        }
                    }
                });
    }

    public void getAppliances(){
        db.collection("receipts")
                .whereEqualTo("Category","Appliances")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        double food=0;
                        for(QueryDocumentSnapshot document: task.getResult()){
                            String Price = document.getString("Total");
                            Log.d(TAG,"String is"+Price);
                            Double price = Double.parseDouble(Price);
                            Log.d(TAG,"double is"+price);

                            food += price;
                            Appliances = food;
                            Log.d(TAG,"Food is"+Food);
                        }
                    }
                });

    }


    public void getEntertainment(){
        db.collection("receipts")
                .whereEqualTo("Category","Entertainment")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        double food=0;
                        for(QueryDocumentSnapshot document: task.getResult()){
                            String Price = document.getString("Total");
                            Log.d(TAG,"String is"+Price);
                            Double price = Double.parseDouble(Price);
                            Log.d(TAG,"double is"+price);

                            food += price;
                            Entertainment = food;
                            Log.d(TAG,"Food is"+Food);
                        }
                    }
                });

    }


    public void getHealthcare(){
        db.collection("receipts")
                .whereEqualTo("Category","Healthcare")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        double food=0;
                        for(QueryDocumentSnapshot document: task.getResult()){
                            String Price = document.getString("Total");
                            Log.d(TAG,"String is"+Price);
                            Double price = Double.parseDouble(Price);
                            Log.d(TAG,"double is"+price);

                            food += price;
                            Healthcare = food;
                            Log.d(TAG,"Food is"+Food);
                        }
                    }
                });

    }



}