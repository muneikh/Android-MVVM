package com.example.muneikh.service;

import com.example.muneikh.model.RepositoryModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GithubApi {
    @GET("/users/{username}/repos")
    Observable<Response<List<RepositoryModel.Repository>>> getRepositoryList(
            @Path("username") String username,
            @Query("page") int pageNumber,
            @Query("per_page") int repoPerPage
    );
}