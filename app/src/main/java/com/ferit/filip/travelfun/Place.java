package com.ferit.filip.travelfun;

 public class Place {

    double latitude;
    double longitude;
    int id;
    String name;
    String text;
    String picture;


    private Place(){



    }

    public Place(double latitude,double longitude, int id, String name,String tekst,String picture){

this.latitude=latitude;
this.longitude=longitude;
this.id=id;
this.name=name;
this.text=tekst;
this.picture=picture;

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


 }
