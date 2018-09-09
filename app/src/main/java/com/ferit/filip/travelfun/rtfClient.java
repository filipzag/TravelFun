package com.ferit.filip.travelfun;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class rtfClient {

    private static final String baseUrl="http://filipz0203.000webhostapp.com/";

 //   private static  retrofit;

    public static  Retrofit getrtfClient(){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


return retrofit;
    }
}
