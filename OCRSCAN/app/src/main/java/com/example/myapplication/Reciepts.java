package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Reciepts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Reciepts extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ListView listView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Reciepts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Reciepts.
     */
    // TODO: Rename and change types and number of parameters
    public static Reciepts newInstance(String param1, String param2) {
        Reciepts fragment = new Reciepts();
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
        View view= inflater.inflate(R.layout.fragment_reciepts, container, false);

        listView = (ListView) view.findViewById(R.id.listview);

        List<infoModel> infoModelList = new ArrayList<>();

        db.collection("receipts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                infoModelList.add(new infoModel(document.getString("Title"), document.getString("Date"), document.getString("Image") ));
                            }

                            infoAdapter infoAdapter = new infoAdapter(getContext(),infoModelList);

                            listView.setAdapter(infoAdapter);
                        }
                    }
                });{

        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                infoModel item = (infoModel) adapterView.getItemAtPosition(i);

                String ID = item.getID();

                loadreciept(ID);
            }
        });

        return view;
    }

    public void loadreciept(String ID){
        db.collection("receipts")
                .whereEqualTo(FieldPath.documentId(),ID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){

                                String Name = document.getString("Title");
                                String category = document.getString("Category");
                                String Date = document.getString("Date");
                                String Total = document.getString("Total");
                                String Imageref = document.getString("Image");



                                storereciept Storereceipt = new storereciept();

                                Bundle attributes = new Bundle();

                                attributes.putString("bundle","loading");

                                attributes.putString("title",Name);

                                attributes.putString("Category",category);

                                attributes.putString("date",Date);

                                attributes.putString("Total",Total);

                                attributes.putString("Imageref",Imageref);

                                Storereceipt.setArguments(attributes);


                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.flFragment,Storereceipt)
                                        .addToBackStack(null) //move to new fragment with bundle.
                                        .commit();







                            }
                        }
                    }
                });
    }
}