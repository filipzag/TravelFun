package com.ferit.filip.travelfun;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface rtfLikeInterface {
    @FormUrlEncoded
    @POST("receiveLikeFromApp.php")
    Call <LikeClass> updateLikes(@Field("id")int id,@Field("likes") int likes);


}

