package com.ferit.filip.travelfun;

import java.io.Serializable;

public class Place implements Serializable{

    double latitude;
    double longitude;
    int id;
    String name;
    String text;
    String picture;
    int likes;

    private Place(){



    }

    public Place(double latitude,double longitude, int id, String name,String tekst,String picture,int likes){

this.latitude=latitude;
this.longitude=longitude;
this.id=id;
this.name=name;
this.text=tekst;
this.picture=picture;
this.likes=likes;

    }


     public double getplaceLatitude() {
         return latitude;
     }

     public double getplaceLongitude() {
         return longitude;
     }

     public int getId() {
        return id;
    }

    public String getUrl() {
        return picture;
    }


     public String getName() {
         return name;
     }


     public String getTekst() {
         return text;
     }

    public int getLikes() {
        return likes;
    }
}
