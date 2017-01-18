package com.jkkniugmail.rubel.tourfriend.tasks;

import com.jkkniugmail.rubel.tourfriend.models.pojo.direction.Direction;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Bdjobs on 11/1/2017.
 */

public interface DirectionApi {
    @GET("")
    Call<Direction> getDirection(@Url String urlString);
}
