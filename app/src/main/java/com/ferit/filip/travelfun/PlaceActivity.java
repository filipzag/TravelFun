package com.ferit.filip.travelfun;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class PlaceActivity extends AppCompatActivity {

    int id;
    int likes;
    String likesString;
    FloatingActionButton fab;
    double queryPlaceLatitude;
    double queryPlaceLongitude;
    @BindView(R.id.name) TextView placeName;
    @BindView(R.id.txt)   TextView tekst;
    @BindView(R.id.pic) ImageView placeImage;
    @BindView(R.id.likestatus) TextView likeStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        ButterKnife.bind(this);
        fab=(FloatingActionButton)findViewById(R.id.fab);
                Bundle bundleObject = getIntent().getExtras();

        ArrayList <Place> secondPlaceList = (ArrayList<Place>) bundleObject.getSerializable("places");
         queryPlaceLatitude= getIntent().getExtras().getDouble("LATITUDE");
         queryPlaceLongitude= getIntent().getExtras().getDouble("LONGITUDE");
for(int i=0;i<secondPlaceList.size();i++){


    if(secondPlaceList.get(i).getplaceLongitude()==queryPlaceLongitude && secondPlaceList.get(i).getplaceLatitude()==queryPlaceLatitude){


        placeName.setText(secondPlaceList.get(i).getName().toString());
        tekst.setText(secondPlaceList.get(i).getTekst().toString());
        Picasso.get().load(secondPlaceList.get(i).getUrl()).into(placeImage);
        id=secondPlaceList.get(i).getId();
        likes=secondPlaceList.get(i).getLikes();

        likesString= likes +" ljudi se sviđa pogled sa ovoga mjesta.";
likeStatus.setText(likesString);


    }



}

createFabListener();

         }


         private void createFabListener(){

        if(fab!=null){


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    updateLikes();
                }
            });





        }


    }

    private void updateLikes() {
        rtfLikeInterface rtfLikeInterface = rtfClient.getrtfClient().create(rtfLikeInterface.class);

        retrofit2.Call<LikeClass> call= rtfLikeInterface.updateLikes(id,likes);

        call.enqueue(new Callback<LikeClass>() {
            @Override
            public void onResponse(Call<LikeClass> call, Response<LikeClass> response) {
                LikeClass likeClass= response.body();
                Toast.makeText(PlaceActivity.this,"Server response:"+likeClass.getResponse(),Toast.LENGTH_LONG).show();
              likes++;
                likesString= likes +" ljudi se sviđa pogled sa ovoga mjesta.";
                likeStatus.setText(likesString);
                fab.setEnabled(false);
            }

            @Override
            public void onFailure(Call<LikeClass> call, Throwable t) {
                    Log.d("DEV","no response from server");
            }
        });


    }

}

