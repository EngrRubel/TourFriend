package com.jkkniugmail.rubel.tourfriend;

/**
 * Created by islan on 12/23/2016.
 */

public class Constants {
    //sign activity
    public static final int SING_IN_ACTIVITY = 0;
    public static final int SING_UP_ACTIVITY = 1;


    //result
    public static final int RESULT_FAIL = 0;
    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_CANCEL = 2;

    public static final int EVENT_FOUND = 1;
    public static final int EVENT_NOT_FOUND = 0;


    public static final String USER_ID = "user_id";
    public static final String EVENT_ID = "event_id";
    public static final String MOMENT_ID = "moment_id";

    //weather
    public static final String BASE_URL_WEATHER = "http://api.openweathermap.org/";
    public static final String BASE_URL_DIRECTION = "https://maps.googleapis.com/maps/api/directions/";
    public static final String BASE_URL_NEARBY_PLACE = "https://maps.googleapis.com/maps/api/place/nearbysearch/";


    public static final String API_KEY_DIRECTION ="AIzaSyDDTRUG0B8cZuQJx00QBq9pjWQl2OoPi4I";
    public static final String API_KEY_WEATHER ="621d60d4900ccc5a8f1b36351a2ba4c5";//weather
    public static final String CURRENT_WEATHER = "current_weather";
    public static final String FORECAST_WEATHER = "forecast_weather";
    public static final String UNIT_METRIC = "metric";

    public static final String CITY_NAME = "city_name";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    public static final String DEFAULT_CITY = "Dhaka";
    public static final Double DEFAULT_LATITUDE = 23.750839;
    public static final Double DEFAULT_LONGITUDE = 90.393307;

    //request_code

    public static final int REQUEST_CAMERA = 1;
    public static final String IMAGE_URL = "image_url";


}
