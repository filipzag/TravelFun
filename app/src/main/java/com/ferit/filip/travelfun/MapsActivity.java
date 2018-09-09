package com.ferit.filip.travelfun;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.WidgetContainer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.io.Serializable;

import butterknife.BindView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private FusedLocationProviderClient mLastLocationFusedClient;
    private LocationCallback mLocationCallback;
    double userSpeed;
    private static int UPDATE_INTERVAL = 1000;// 1 SEKUNDA
    private static int DISPLACEMENT = 5;

    //NotificationHelper helper;
    DatabaseReference ref;

    GeoFire geoFire;
    Marker myCurrent;
    List<Place> placeList = new ArrayList<>();

    private String server_url = "http://filipz0203.000webhostapp.com/androidDatabaseSync.php";

    private static final int MY_PERMISSION_REQUEST_CODE = 2396;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 5821;

    LocationRequest mLocationRequest = new LocationRequest();

    FloatingActionButton fabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ref = FirebaseDatabase.getInstance().getReference("myLocation");
        fabButton = (FloatingActionButton) findViewById(R.id.fabBtn);
        geoFire = new GeoFire(ref);

        setUpLocation();
        createLocationCallback();
        createFabListener();

    }

    private void createFabListener() {

if(fabButton!=null){
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openReportActivity();


            }
        });}
    }

    private void openReportActivity() {

        Intent intentReport = new Intent(this, ReportPlace.class);


        intentReport.putExtra("LATITUDE", mLastLocation.getLatitude());
        intentReport.putExtra("LONGITUDE",mLastLocation.getLongitude());
       startActivity(intentReport);

    }


    private void setUpLocation() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{

                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_CODE);


        } else {

            if (checkPlayService()) {

                buildGoogleApiClient();
                createLocationRequest();


            }
        }


    }


     private void createLocationCallback(){

        mLocationCallback = new LocationCallback(){

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    userSpeed=location.getSpeed();

                    Log.d("DEV-callback", String.format("Korisnikova brzina je: %f",userSpeed));


displayLocation();







                    Log.d("DEV-callback", String.format("Yout location has changed: %f /%f", latitude, longitude));
                    if (location == null) {
                        Log.d("DEV","Can't get your location!");
                    }
                }
            }
        };
     }


    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return;
        }


        mLastLocationFusedClient = LocationServices.getFusedLocationProviderClient(this);

        mLastLocationFusedClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

if(location!=null){
    mLastLocation=location;
    userSpeed=location.getSpeed();
                            final double latitude = location.getLatitude();
                            final double longitude = location.getLongitude();

                       // Update na Firebase
                            geoFire.setLocation("You", new GeoLocation(latitude, longitude),
                                    new GeoFire.CompletionListener() {
                                        @Override
                                        public void onComplete(String key, DatabaseError error) {



                                            //Add marker
                                    if(myCurrent!=null){


                                            myCurrent.remove();
                                            myCurrent = mMap.addMarker(new MarkerOptions()
                                                    .position((new LatLng(latitude, longitude)))
                                                    .title("You"));

                                            //Move camera to this position

                                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude),12.0f));
                                            ;

                                        }else {

                                        myCurrent = mMap.addMarker(new MarkerOptions()
                                                .position((new LatLng(latitude, longitude)))
                                                .title("You"));

                                        //Move camera to this position

                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));
                                        ;
                                    }
                                        }
                                    });




                        Log.d("EMDTDEV", String.format("Yout location has changed: %f /%f", latitude, longitude));
                        if (location == null) {
                            Log.d("EDMTDEV","Can't get your location!");
                        }

                    }}
                });

    }

    public void createLocationRequest() {



        mLocationRequest.setInterval(UPDATE_INTERVAL);
        long FASTEST_INTERVAL = 3000;
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);


    }
    private void buildGoogleApiClient() {


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private boolean checkPlayService() {
        int ResultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (ResultCode != ConnectionResult.SUCCESS) {

            if (GoogleApiAvailability.getInstance().isUserResolvableError(ResultCode)) {
                GoogleApiAvailability.getInstance().getErrorDialog(this,ResultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {


                Toast.makeText(this, "This device is not supported", Toast.LENGTH_SHORT).show();
                finish();

            }
            return false;
        }
        return true;
    }







    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

   //kreiranje područja znamenitosti





        StringRequest stringRequest=new StringRequest(Request.Method.POST,server_url,
                new Response.Listener<String>(){


                    @Override
                    public void onResponse(String response) {


                        GsonBuilder builder= new GsonBuilder();
                        Gson gson= builder.create();
                        placeList= Arrays.asList(gson.fromJson(response,Place[].class));


                        Log.d("DEV", String.format("size  in getInfo is: %d ",placeList.size()));





                        Log.d("DEV", String.format("Size in OnMapReady is: %d ",placeList.size()));
                        double arrayOfPlaces[][]=new double[placeList.size()][placeList.size()];

                        for(int j=0;j<placeList.size();j++){


                            arrayOfPlaces[0][j]= placeList.get(j).getplaceLongitude();

                            arrayOfPlaces[1][j]=placeList.get(j).getplaceLatitude();




                        }




                        GeoQuery geoQuery;

                        for(int i=0;i<arrayOfPlaces.length;i++) {


                            Log.d("DEV", String.format("latitude and longitude is: %f %f", arrayOfPlaces[0][i], arrayOfPlaces[1][i]));

                            LatLng znamenitost = new LatLng(arrayOfPlaces[1][i], arrayOfPlaces[0][i]);
                            mMap.addCircle(new CircleOptions()
                                    .center(znamenitost)
                                    .radius(500)
                                    .strokeColor(Color.BLUE)
                                    .fillColor(0x220000FF)
                                    .strokeWidth(5.0f)
                            );

Log.d("DEV",String.format("Korisnikova brzina je %f",userSpeed));
if(userSpeed<1.75){


    geoQuery = geoFire.queryAtLocation(new GeoLocation(znamenitost.latitude, znamenitost.longitude), 0.3f); // 0.5f=0.5km




}else {


    geoQuery = geoFire.queryAtLocation(new GeoLocation(znamenitost.latitude, znamenitost.longitude), 1.0f); // 0.5f=0.5km


}

                            // Add GeoQuery listener


                            final GeoQuery finalGeoQuery = geoQuery;
                            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                                @Override
                                public void onKeyEntered(String key, GeoLocation location) {

                                    sendNotification("TravelFun", String.format("%s entered area with interesting view", key), finalGeoQuery.getCenter().latitude, finalGeoQuery.getCenter().longitude);


                                    Log.d("DEV", String.format("centar ovog querya je u lat lng %f %f", finalGeoQuery.getCenter().latitude, finalGeoQuery.getCenter().longitude));


                                }

                                @Override
                                public void onKeyExited(String key) {

                                }

                                @Override
                                public void onKeyMoved(String key, GeoLocation location) {


                                }

                                @Override
                                public void onGeoQueryReady() {

                                }

                                @Override
                                public void onGeoQueryError(DatabaseError error) {


                                    Log.d("ERROR", "" + error);
                                }
                            });

                        }






                    }
                },new Response.ErrorListener(){


            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(this).addToRequestQue(stringRequest);


    }

    private void sendNotification(String title, String content,double queryLatitude,double queryLongitude) {


        Notification.Builder builder= new Notification.Builder(this)
                .setSmallIcon(R.drawable.logo_round)
                .setContentTitle(title)
                .setContentText(content);
        NotificationManager manager= (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        //helper= new NotificationHelper(this);
      //  Notification.Builder builder2=helper.getFFChannelNotification(title,content);



        Intent intent=new Intent(this,PlaceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("places", (Serializable) placeList);
        intent.putExtra("LATITUDE",queryLatitude);
        intent.putExtra("LONGITUDE",queryLongitude);
        intent.putExtras(bundle);

        PendingIntent contentIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        Notification notification= builder.build();
        //builder2.setContentIntent(contentIntent);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;

        manager.notify(new Random().nextInt(),notification);
        //helper.getManager().notify(new Random().nextInt(),builder2.build());



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){

            case MY_PERMISSION_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){


                    if (checkPlayService()) {

                        buildGoogleApiClient();
                        createLocationRequest();
                        displayLocation();
                    }
                }
                break;
        }}

    @Override
    public void onLocationChanged(Location location) {


        mLastLocation=location;
        userSpeed=location.getSpeed();
        Log.d("DEV",String.format("Korisnikova brzina je %f",userSpeed));
        displayLocation();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        displayLocation();
        startLocationUpdates();


    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        mLastLocationFusedClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

    }

    @Override
    public void onConnectionSuspended(int i) {

        mGoogleApiClient.connect();



    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    }






