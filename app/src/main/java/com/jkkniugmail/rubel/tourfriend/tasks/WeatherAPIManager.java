package com.jkkniugmail.rubel.tourfriend.tasks;


import com.jkkniugmail.rubel.tourfriend.models.pojo.current.CurrentWeather;
import com.jkkniugmail.rubel.tourfriend.models.pojo.forecast.ForecastWeather;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.jkkniugmail.rubel.tourfriend.Constants.BASE_URL_WEATHER;


public class WeatherAPIManager {


    public interface CurrentWeatherAPI {
        @GET("data/2.5/weather?")
        Call<CurrentWeather> getWheatherReport(
                @Query("q") String city,
                @Query("units") String unit,
                @Query("appid") String appid);

    }

    public static CurrentWeatherAPI getCurrentWeatherAPIService () {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_WEATHER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(CurrentWeatherAPI.class);


    }


    public interface ForecastWeatherAPI {
        @GET("data/2.5/forecast/daily?")
        Call<ForecastWeather> getForecastWheatherReport(@Query("q") String city,
                                                        @Query("units") String unit,
                                                        @Query("cnt") int day,
                                                        @Query("appid") String appid);
    }

    public static ForecastWeatherAPI getForecastWeatherAPIService (){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_WEATHER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ForecastWeatherAPI.class);
    }

}