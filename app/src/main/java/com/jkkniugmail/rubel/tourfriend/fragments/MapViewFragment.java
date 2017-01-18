package com.jkkniugmail.rubel.tourfriend.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
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


public class MapViewFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private MapView mMapView;
    private GoogleMap googleMap;
    private String city_name;
    private double latitute;
    private double longitute;
    private LatLng currentLatLng;
    private DirectionApi directionApi;
    private Direction direction;

    private EditText destCityET;
    private Button searchBTN;


    public MapViewFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MapViewFragment newInstance(double latitute, double longitute, String city_name) {
        MapViewFragment fragment = new MapViewFragment();
        Bundle args = new Bundle();
        args.putDouble(Constants.LATITUDE, latitute);
        args.putDouble(Constants.LONGITUDE, longitute);
        args.putString(Constants.CITY_NAME, city_name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //
            latitute = getArguments().getDouble(Constants.LATITUDE);
            longitute = getArguments().getDouble(Constants.LONGITUDE);
            city_name = getArguments().getString(Constants.CITY_NAME);
        }
        else{
            latitute = Constants.DEFAULT_LATITUDE;
            longitute = Constants.DEFAULT_LONGITUDE;
            city_name = Constants.DEFAULT_CITY;
        }
        currentLatLng = new LatLng(latitute, longitute);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_DIRECTION)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        directionApi = retrofit.create(DirectionApi.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        destCityET = (EditText) view.findViewById(R.id.dest_city_et);
        searchBTN = (Button) view.findViewById(R.id.search_directionBTN);
        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dest_city = destCityET.getText().toString();
                if (TextUtils.isEmpty(dest_city)){
                    destCityET.setError("Type a city");
                }
                else {
                    getDirection(dest_city);
                }
            }
        });

        Toast.makeText(getContext(), city_name, Toast.LENGTH_LONG).show();

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                Toast.makeText(getContext(), "map found", Toast.LENGTH_LONG).show();

                // For dropping a marker at a point on the Map
                UiSettings uiSettings = googleMap.getUiSettings();
                uiSettings.setAllGesturesEnabled(true);
                uiSettings.setZoomControlsEnabled(true);


                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                googleMap.addMarker(new MarkerOptions().position(currentLatLng).title(city_name));
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void getDirection(String destCity) {

        String link = "json?origin=" + city_name+ "&destination=" +
                destCity + "&mode=walking&key=" + Constants.API_KEY_DIRECTION;
        String urlString = String.format(link);
        Call<Direction> directionCall = directionApi.getDirection(urlString);
        directionCall.enqueue(new Callback<Direction>() {
            @Override
            public void onResponse(Call<Direction> call, Response<Direction> response) {
                if (response != null && response.errorBody() == null) {
                    direction = response.body();
                    drawDirection();

                } else {
                    Toast.makeText(getContext(),"Failed, pleased try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Direction> call, Throwable t) {
                Toast.makeText(getContext(),"Please check your connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void drawDirection() {

        PolylineOptions lines = new PolylineOptions();
        lines.add(currentLatLng);
        lines.color(Color.BLUE);
        lines.width(10);
        List<Step> steps = direction.getRoutes().get(0).getLegs().get(0).getSteps();
        LatLng endLatLng = null;

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
            if (i==steps.size()-1){
                endLatLng = latLngE;
            }
        }



        googleMap.addPolyline(lines);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(endLatLng, 15));
        googleMap.addMarker(new MarkerOptions().position(endLatLng).title(direction.getRoutes().get(0).getLegs().get(0).getEndAddress()));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
