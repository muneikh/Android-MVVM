package com.example.muneikh;

import com.example.muneikh.model.RepositoryModel;
import com.example.muneikh.service.GithubApi;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.mock.BehaviorDelegate;

public class GithubApiMock implements GithubApi {

    private final BehaviorDelegate<GithubApi> delegate;

    public GithubApiMock(BehaviorDelegate<GithubApi> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Observable<Response<List<RepositoryModel.Repository>>> getRepositoryList(
            @Path("username") String username, @Query("page") int pageNumber,
            @Query("per_page") int repoPerPage, @Query("access_token") String accessToken) {

        return delegate.returningResponse(SampleResponse.RESPONSE_1).getRepositoryList(username, pageNumber,
                repoPerPage, accessToken);
    }
}
