package com.soulfly.umorili.rest;


import com.soulfly.umorili.rest.models.QueryServerModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface UmoriliAPI {

    @GET("get")
    Call<List<QueryServerModel>> response(@Query("site") String site,
                                          @Query("name") String name,
                                          @Query("num") int num);

}
