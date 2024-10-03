package com.example.myapplication;

public class infoModel {


    private String Title;

    private String description; //creates attributes

    private String ID;

    public infoModel(String Title, String Description, String ID){
        this.Title = Title;
        this.description = Description; //accesses private attributes to be used.
        this.ID = ID;

    }



    public String gettitle(){
        return Title;
    } //returns attributes.

    public String getDescription(){
        return description;
    }

    public String getID(){return ID;}

}
