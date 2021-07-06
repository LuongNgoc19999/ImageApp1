package com.example.imageapp.server;

import com.example.imageapp.model.Data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GetDataService {
    @FormUrlEncoded
    @POST("/predictcar")
    void getLandingPageReport(@Field("image") String code);
}
