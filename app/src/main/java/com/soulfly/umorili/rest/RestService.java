package com.soulfly.umorili.rest;


import android.support.annotation.NonNull;

import com.soulfly.umorili.rest.models.QueryServerModel;

import java.io.IOException;
import java.util.List;


public final class RestService {

    private RestClient restClient;

    public static RestService getInstance() {
        if(instance == null){
            instance = new RestService();
        }
        return instance;
    }

    private static RestService instance;

    public RestService() {
        restClient = new RestClient();
    }

    public List<QueryServerModel> queryResponse(@NonNull String site,
                                                @NonNull String name,
                                                @NonNull int num) throws IOException {

        return restClient
                .getUmoriliAPI()
                .response(site, name, num)
                .execute()
                .body();

    }
}
