package com.jkkniugmail.rubel.tourfriend.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkkniugmail.rubel.tourfriend.R;
import com.jkkniugmail.rubel.tourfriend.models.pojo.forecast.List;
import com.jkkniugmail.rubel.tourfriend.models.pojo.forecast.Weather;
import com.jkkniugmail.rubel.tourfriend.utils.TimeStamp;
import com.squareup.picasso.Picasso;

/**
 * Created by islan on 12/19/2016.
 */

public class ForeastAdapter extends ArrayAdapter<List> {
    Context context;
    List forecastWeather;
    java.util.List<List> forecastWeatherArrayList;

    public ForeastAdapter(Context context, java.util.List<List> forecastWeatherArrayList) {
        super(context, R.layout.row_list_weather, forecastWeatherArrayList);
        this.context = context;
        this.forecastWeatherArrayList = forecastWeatherArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderForecast viewHolderForecast;
        forecastWeather = forecastWeatherArrayList.get(position);
        if (convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_list_weather, parent, false);
            viewHolderForecast = new ViewHolderForecast(convertView);
            convertView.setTag(viewHolderForecast);
        }
        else {
            viewHolderForecast = (ViewHolderForecast) convertView.getTag();
        }

        // *its a great fault of openWeather*
        java.util.List<Weather> weathers = forecastWeather.getWeather();
        Weather weather = weathers.get(0);
        TimeStamp t = new TimeStamp();


        String icon = "http://openweathermap.org/img/w/"+weather.getIcon().toString()+".png";

        viewHolderForecast.date.setText(t.getDateString(forecastWeather.getDt()));
        viewHolderForecast.press.setText(forecastWeather.getPressure().toString()+"hPa pressure");
        viewHolderForecast.temp_day.setText(forecastWeather.getTemp().getDay().toString()+"°C");
        viewHolderForecast.temp_night.setText(forecastWeather.getTemp().getNight().toString()+"°C");
        viewHolderForecast.wind.setText(forecastWeather.getSpeed().toString()+"m/s wind speed");
        viewHolderForecast.dscr.setText(weather.getDescription().toString());
        viewHolderForecast.cloud.setText(forecastWeather.getClouds().toString() + " % cloudy");
        viewHolderForecast.hum.setText(forecastWeather.getHumidity().toString() + " % humidity");
        Picasso.with(context).load(icon).into(viewHolderForecast.icn);

        return convertView;
    }

    public class ViewHolderForecast {

        TextView date;
        TextView dscr;
        TextView temp_day;
        TextView wind;
        TextView press;
        TextView temp_night;
        TextView hum;
        TextView cloud;
        ImageView icn;


        ViewHolderForecast(View view){
            date = (TextView) view.findViewById(R.id.fc_date);
            dscr = (TextView) view.findViewById(R.id.fc_dsc);
            temp_day = (TextView) view.findViewById(R.id.fc_tmp_day);
            temp_night = (TextView) view.findViewById(R.id.fc_tmp_night);
            wind = (TextView) view.findViewById(R.id.fc_wind);
            press = (TextView) view.findViewById(R.id.fc_prss);
            hum = (TextView) view.findViewById(R.id.fc_hum);
            cloud = (TextView) view.findViewById(R.id.fc_cloud);
            icn = (ImageView) view.findViewById(R.id.fc_icn);

        }
    }
}







