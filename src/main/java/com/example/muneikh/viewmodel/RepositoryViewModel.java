package com.example.muneikh.viewmodel;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.muneikh.model.RepositoryModel;
import com.example.muneikh.service.ServiceProvider;
import com.example.muneikh.utility.PageLinks;
import com.example.muneikh.utility.SharedPreferenceManager;
import com.example.muneikh.utility.URLParser;
import com.google.gson.Gson;

import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class RepositoryViewModel extends Observable {

    private static final String TAG = "RepositoryViewModel";
    private static final String USERNAME = "google";
    private static final String PARAM_PAGE = "page";
    private static final int REPO_PER_PAGE = 10;

    private boolean loading = false;

    private RepositoryModel repositoryModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SharedPreferenceManager sharedPreference;

    public RepositoryViewModel(Context context) {
        sharedPreference = new SharedPreferenceManager(context);
    }

    public void restoreData() {
        RepositoryModel savedRepositoryModel = sharedPreference.getRepositoryModel();
        if (savedRepositoryModel == null) {
            repositoryModel = new RepositoryModel();
            fetchRepositories();
        } else {
            repositoryModel = savedRepositoryModel;
            notifyChange();
        }
    }

    public void onRecyclerViewScrolled(LinearLayoutManager layoutManager, int dy) {
        if (dy > 0) {
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

            if (!loading) {
                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    fetchRepositories();
                }
            }
        }
    }

    private void fetchRepositories() {
        if (repositoryModel.getNextPage() != null) {
            loading = true;
            ServiceProvider serviceProvider = ServiceProvider.getInstance();
            serviceProvider.githubApi.getRepositoryList(USERNAME, repositoryModel.getNextPage(),
                    REPO_PER_PAGE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        changeRepositoryDataSet(response);
                        loading = false;
                    });
        }
    }

    private void changeRepositoryDataSet(Response<List<RepositoryModel.Repository>> response) {
        PageLinks pageLinks = new PageLinks(response.headers());
        Integer pageCount = parseStringFromInt(URLParser.getParamFromURL(pageLinks.getLast(), PARAM_PAGE));
        repositoryModel.setTotalPages(pageCount);

        Integer nextPage = parseStringFromInt(URLParser.getParamFromURL(pageLinks.getNext(), PARAM_PAGE));
        repositoryModel.setNextPage(nextPage);

        List<RepositoryModel.Repository> repositories = response.body();
        Log.d(TAG, "toJson: " + new Gson().toJson(repositories));
        if (repositories != null) {
            repositoryModel.getRepositories().addAll(repositories);
            notifyChange();
            sharedPreference.saveRepositoryModel(repositoryModel);
        }
    }

    private void notifyChange() {
        setChanged();
        notifyObservers();
    }

    private Integer parseStringFromInt(String stringValue) {
        Integer value = null;
        try {
            value = Integer.parseInt(stringValue);
        } catch (NumberFormatException e) {
            Log.e(TAG, "changeRepositoryDataSet: " + e.getLocalizedMessage());
        }
        return value;
    }

    public RepositoryModel getRepositoryModel() {
        return repositoryModel;
    }

    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
    }
}
