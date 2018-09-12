package com.ferit.filip.travelfun;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface rtfInterface {
@FormUrlEncoded
@POST("recieveDataFromApp.php")
Call <ImageClass> uploadImage(@Field("title")String title,@Field("txt") String txt,@Field("latitude") double lat, @Field("longitude") double lng,@Field("image") String image);


}
