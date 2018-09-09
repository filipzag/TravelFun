package com.ferit.filip.travelfun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceActivity extends AppCompatActivity {


    double queryPlaceLatitude;
    double queryPlaceLongitude;
    @BindView(R.id.name) TextView placeName;
    @BindView(R.id.txt)   TextView tekst;
    @BindView(R.id.pic) ImageView placeImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        ButterKnife.bind(this);

Bundle bundleObject = getIntent().getExtras();

        ArrayList <Place> secondPlaceList = (ArrayList<Place>) bundleObject.getSerializable("places");
         queryPlaceLatitude= getIntent().getExtras().getDouble("LATITUDE");
         queryPlaceLongitude= getIntent().getExtras().getDouble("LONGITUDE");
for(int i=0;i<secondPlaceList.size();i++){


    if(secondPlaceList.get(i).getplaceLongitude()==queryPlaceLongitude && secondPlaceList.get(i).getplaceLatitude()==queryPlaceLatitude){


placeName.setText(secondPlaceList.get(i).getName().toString());
tekst.setText(secondPlaceList.get(i).getTekst().toString());
        Picasso.get().load(secondPlaceList.get(i).getUrl()).into(placeImage);




    }



}


         }

    }

