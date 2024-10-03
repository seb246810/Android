package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class infoAdapter extends ArrayAdapter<infoModel>{
    public infoAdapter(Context context, List<infoModel> infoModelList){

        super(context,0,infoModelList);



    }

    @Override
    public View getView(int position, View Convertview, ViewGroup parent){
        if(Convertview == null){
            Convertview = LayoutInflater.from(getContext()).inflate(R.layout.info_list_item,parent,false);
        }

        infoModel info = getItem(position); //used for if an item is clicked or selected in list.
        TextView titleTextView = Convertview.findViewById(R.id.titleTextview);
        TextView descriptionTextView = Convertview.findViewById(R.id.Descriptiontextview);



        if(info != null){
            titleTextView.setText(info.gettitle());
            descriptionTextView.setText(info.getDescription()); //sets list view with title and descriptions
        }

        return Convertview;
    }
}
