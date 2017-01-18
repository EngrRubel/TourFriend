package com.jkkniugmail.rubel.tourfriend.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jkkniugmail.rubel.tourfriend.Constants;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by islan on 12/20/2016.
 */

public class MySharedPreferences {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public MySharedPreferences(Context context){
        sharedPreferences = context.getSharedPreferences("user", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }



    public void saveLastMoment(String directory){
        editor.putString(Constants.IMAGE_URL, directory);
        editor.commit();
    }

    public String getLastMoment(){
        return sharedPreferences.getString(Constants.IMAGE_URL,"");
    }

    public void saveUser(int user_id){
        editor.putInt(Constants.USER_ID, user_id);
        editor.commit();
    }
    public void unsaveUser(){
        editor.remove(Constants.USER_ID);
        editor.commit();
    }
    public int getUser(){
        int user_id = sharedPreferences.getInt(Constants.USER_ID, -1);
        return user_id;
    }
}
