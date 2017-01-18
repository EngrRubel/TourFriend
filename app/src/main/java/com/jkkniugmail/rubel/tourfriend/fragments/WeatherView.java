package com.jkkniugmail.rubel.tourfriend.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jkkniugmail.rubel.tourfriend.Constants;
import com.jkkniugmail.rubel.tourfriend.R;
import com.jkkniugmail.rubel.tourfriend.fragments.weather_child.CurrentWeatherFragment;
import com.jkkniugmail.rubel.tourfriend.fragments.weather_child.ForecastWeatherFragment;


public class WeatherView extends Fragment {
    private Button currentBTN;
    private Button forcastBTN;
    private FragmentManager fragmentManager;
    Fragment fragment;
    private OnFragmentInteractionListener mListener;

    public  String city_name;

    public WeatherView() {
        // Required empty public constructor
    }

    public static WeatherView newInstance(String city_name) {
        WeatherView fragment = new WeatherView();
        Bundle args = new Bundle();
        args.putString(Constants.CITY_NAME, city_name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.city_name = getArguments().getString(Constants.CITY_NAME);

        }
        else {
            //error
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentManager = getChildFragmentManager();
        fragment = CurrentWeatherFragment.newInstance(city_name);
        fragmentManager.beginTransaction().add(R.id.weather_fragment, fragment, "current").commit();
        currentBTN = (Button) view.findViewById(R.id.currentBtn);
        forcastBTN = (Button) view.findViewById(R.id.forecastBtn);
        currentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = CurrentWeatherFragment.newInstance(city_name);
                fragmentManager.beginTransaction().replace(R.id.weather_fragment, fragment, "current").commit();
            }
        });

        forcastBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = ForecastWeatherFragment.newInstance(city_name);
                fragmentManager.beginTransaction().replace(R.id.weather_fragment, fragment, "forecast").commit();
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
