package com.example.muneikh;

import com.example.muneikh.service.GithubApi;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.observers.TestObserver;
import retrofit2.Retrofit;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

import static junit.framework.Assert.assertEquals;

public class GithubApiTest {

    private final NetworkBehavior behavior = NetworkBehavior.create();
    private final TestObserver testObserver = TestObserver.create();
    private GithubApi githubApi;

    @Before
    public void setUp() throws Exception {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BuildConfig.BASE_URL).build();

        MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior).build();

        final BehaviorDelegate<GithubApi> delegate = mockRetrofit.create(GithubApi.class);
        githubApi = new GithubApiMock(delegate);
    }

    @Test
    public void testSuccessResponse() throws Exception {
        givenNetworkFailurePercentIs(0);
        githubApi.getRepositoryList("xing", 1, 10, "12345").subscribe(
                response -> assertEquals(SampleResponse.RESPONSE_1, response.body()));
    }

    @Test
    public void testFailureResponse() throws Exception {
        givenNetworkFailurePercentIs(100);
        githubApi.getRepositoryList("xing", 1, 10, "12345").subscribe(testObserver);
        testObserver.assertNoValues();
        testObserver.assertError(IOException.class);
    }

    @Test
    public void testMaxRepoCount() throws Exception {
        givenNetworkFailurePercentIs(0);
        githubApi.getRepositoryList("xing", 1, 10, "12345").subscribe(response ->
                assertEquals(10, response.body().size()));
    }

    private void givenNetworkFailurePercentIs(int failurePercent) {
        behavior.setDelay(0, TimeUnit.MILLISECONDS);
        behavior.setVariancePercent(0);
        behavior.setFailurePercent(failurePercent);
    }
}