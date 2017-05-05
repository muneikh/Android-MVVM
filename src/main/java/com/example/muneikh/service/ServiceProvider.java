package com.example.muneikh.service;

import com.example.muneikh.BuildConfig;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceProvider {

    private static final ServiceProvider instance = new ServiceProvider();

    public final GithubApi githubApi;

    public static ServiceProvider getInstance() {
        return instance;
    }

    private ServiceProvider() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        githubApi = retrofit.create(GithubApi.class);
    }
}
