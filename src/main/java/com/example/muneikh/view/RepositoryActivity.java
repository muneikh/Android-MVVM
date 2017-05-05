package com.example.muneikh.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.muneikh.R;
import com.example.muneikh.viewmodel.RepositoryViewModel;

import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepositoryActivity extends Activity implements Observer {

    @BindView(R.id.recycler_view)
    RecyclerView repositoryRecyclerView;

    private RepositoryViewModel repositoryViewModel;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        ButterKnife.bind(this);

        repositoryViewModel = new RepositoryViewModel(this);
        setupObserver(repositoryViewModel);
        setupRepositoryView(repositoryRecyclerView);
        repositoryViewModel.restoreData();
    }

    @Override
    protected void onDestroy() {
        repositoryViewModel.reset();
        super.onDestroy();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof RepositoryViewModel) {
            RepositoryViewModel repositoryViewModel = (RepositoryViewModel) observable;
            RepositoryAdapter adapter = (RepositoryAdapter) repositoryRecyclerView.getAdapter();
            adapter.setRepositories(repositoryViewModel.getRepositoryModel().getRepositories());
        }
    }

    private void setupRepositoryView(RecyclerView repositoryRecyclerView) {
        RepositoryAdapter adapter = new RepositoryAdapter();
        repositoryRecyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        repositoryRecyclerView.setLayoutManager(layoutManager);

        repositoryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                repositoryViewModel.onRecyclerViewScrolled(layoutManager, dy);
            }
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                layoutManager.getOrientation());
        repositoryRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void setupObserver(Observable observable) {
        observable.addObserver(this);
    }
}