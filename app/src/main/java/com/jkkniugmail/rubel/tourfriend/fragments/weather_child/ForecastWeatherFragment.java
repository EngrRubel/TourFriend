package com.jkkniugmail.rubel.tourfriend.fragments.weather_child;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.jkkniugmail.rubel.tourfriend.Constants;
import com.jkkniugmail.rubel.tourfriend.R;
import com.jkkniugmail.rubel.tourfriend.adapters.ForeastAdapter;
import com.jkkniugmail.rubel.tourfriend.models.pojo.forecast.ForecastWeather;
import com.jkkniugmail.rubel.tourfriend.models.pojo.forecast.List;
import com.jkkniugmail.rubel.tourfriend.tasks.WeatherAPIManager;
import com.jkkniugmail.rubel.tourfriend.utils.MySharedPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jkkniugmail.rubel.tourfriend.Constants.API_KEY_WEATHER;
import static com.jkkniugmail.rubel.tourfriend.Constants.UNIT_METRIC;

/**
 * Created by islan on 12/14/2016.
 */

public class ForecastWeatherFragment extends Fragment {
    Call<ForecastWeather> call;
    ListView listView;
    ForeastAdapter adapter;
    ForecastWeather forecastWeather;
    String city_name;
    MySharedPreferences preferences;

    public static ForecastWeatherFragment newInstance(String city_name) {

        Bundle args = new Bundle();
        args.putString(Constants.CITY_NAME, city_name);
        ForecastWeatherFragment fragment = new ForecastWeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            city_name = getArguments().getString(Constants.CITY_NAME);
        }
        else{
            city_name = Constants.DEFAULT_CITY;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_forecast, container, false);
        listView = (ListView) view.findViewById(R.id.forecast_list);
        getForecastReport(city_name, UNIT_METRIC,15,API_KEY_WEATHER);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        getForecastReport(city_name, UNIT_METRIC,15,API_KEY_WEATHER);
    }

    public void setAdapterOnListView(java.util.List<List> forecastList){
        adapter = new ForeastAdapter(getContext(), forecastList);
        listView.setAdapter(adapter);

    }

    public void getForecastReport(String city, String unit, int forcastDay, String api_key){
        call = WeatherAPIManager.getForecastWeatherAPIService().getForecastWheatherReport(city, unit, forcastDay, api_key);
        call.enqueue(new Callback<ForecastWeather>() {
            @Override
            public void onResponse(Call<ForecastWeather> call, Response<ForecastWeather> response) {
                if (response != null && response.errorBody() == null) {
                    forecastWeather = response.body();
                    setAdapterOnListView(forecastWeather.getList());
                }
                else
                    Toast.makeText(getContext(), "Please try again", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ForecastWeather> call, Throwable t) {
                Toast.makeText(getContext(), "failed", Toast.LENGTH_LONG).show();

            }
        });


    }


}
