package com.ferit.filip.travelfun;
import com.google.gson.annotations.SerializedName;


public class LikeClass {

    @SerializedName("id")

    private int id;

    @SerializedName("likes")

    private int likes;


    @SerializedName("response")

    private String Response;

    public String getResponse() {
        return Response;
    }
}
