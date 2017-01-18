package com.jkkniugmail.rubel.tourfriend.fragments.dialog_fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jkkniugmail.rubel.tourfriend.Constants;
import com.jkkniugmail.rubel.tourfriend.R;
import com.jkkniugmail.rubel.tourfriend.models.pojo.direction.Direction;
import com.jkkniugmail.rubel.tourfriend.models.pojo.direction.Step;
import com.jkkniugmail.rubel.tourfriend.tasks.DirectionApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapFragmentDemo extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng startLocation;
    private LatLng destLocation;
    private LatLng midLocation;
    private DirectionApi directionApi;
    private Direction direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_fragment_demo);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Intent intent = getIntent();
        startLocation = intent.getParcelableExtra("s");
        destLocation = intent.getParcelableExtra("d");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_DIRECTION)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        directionApi = retrofit.create(DirectionApi.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setAllGesturesEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        mMap.addMarker(new MarkerOptions().position(startLocation).title("start point"));

        //mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 15));


//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            if (ActivityCompat.shouldShowRequestPermissionRationale( this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//            }
//            else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//            }
//            if (ActivityCompat.shouldShowRequestPermissionRationale( this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
//            }
//            else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//            }
//        }
//        mMap.setMyLocationEnabled(true);

        getDirection();

    }

    public void getDirection() {

        String link = "json?origin=" + startLocation.latitude+","+startLocation.longitude+ "&destination=" +
                        destLocation.latitude+","+destLocation.longitude + "&mode=walking&key=" + Constants.API_KEY_DIRECTION;
        String urlString = String.format(link);
        Call<Direction> directionCall = directionApi.getDirection(urlString);
        directionCall.enqueue(new Callback<Direction>() {
            @Override
            public void onResponse(Call<Direction> call, Response<Direction> response) {
                if (response != null && response.errorBody() == null) {
                    direction = response.body();
                    drawDirection();

                } else {
                     Toast.makeText(MapFragmentDemo.this,"Failed, pleased try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Direction> call, Throwable t) {
                 Toast.makeText(MapFragmentDemo.this,"Please check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void drawDirection() {



        PolylineOptions lines = new PolylineOptions();
        lines.add(startLocation);
        lines.color(Color.BLUE);
        lines.width(10);
        List<Step> steps = direction.getRoutes().get(0).getLegs().get(0).getSteps();

        for (int i=0; i<steps.size(); i++){
            double lat;
            double lng;
            //steps startLocation
            lat = steps.get(i).getStartLocation().getLat();
            lng = steps.get(i).getStartLocation().getLng();
            LatLng latLngS = new LatLng(lat, lng);
            lines.add(latLngS);
            //steps endlocation
            lat = steps.get(i).getEndLocation().getLat();
            lng = steps.get(i).getEndLocation().getLng();
            LatLng latLngE = new LatLng(lat, lng);
            lines.add(latLngE);

            if (i==steps.size()/2){
                midLocation = latLngE;
            }
        }

        double distance;
        distance = direction.getRoutes().get(0).getLegs().get(0).getDistance().getValue();
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.map_snippet, null);
                TextView summeryTV, distanceTV,timeTV;
                summeryTV = (TextView) v.findViewById(R.id.summery);
                distanceTV = (TextView) v.findViewById(R.id.distance);
                timeTV = (TextView) v.findViewById(R.id.need_time);
                summeryTV.setText(direction.getRoutes().get(0).getSummary().toString());
                distanceTV.setText(direction.getRoutes().get(0).getLegs().get(0).getDistance().getText());
                timeTV.setText(direction.getRoutes().get(0).getLegs().get(0).getDuration().getText());

                return v;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(midLocation, 15));
        mMap.addMarker(new MarkerOptions().position(destLocation).title("end point"));
        mMap.addPolyline(lines);
    }
}
