package com.ferit.filip.travelfun;

import com.google.gson.annotations.SerializedName;

public class ImageClass {


    @SerializedName("title")

    private String title;



    @SerializedName("txt")
    private String txt;


    @SerializedName("latitude")

    private double lat;

    @SerializedName("longitude")

    private double lng;


    @SerializedName("image")

    private String image;



    @SerializedName("response")

    private String Response;

    public String getResponse(){


        return  Response;
    }
}
