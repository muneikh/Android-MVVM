package com.example.muneikh.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.muneikh.model.RepositoryModel;

public class SharedPreferenceManager {

    private static final String SHARED_PREFERENCE_KEY = "DemoApp";
    private static final String REPOSITORY_MODEL_KEY = "repositoryModelKey";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferenceManager(Context app) {
        sharedPreferences = app.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public RepositoryModel getRepositoryModel() {
        String json = sharedPreferences.getString(REPOSITORY_MODEL_KEY, null);
        return RepositoryModel.fromJson(json);
    }

    public void saveRepositoryModel(RepositoryModel repositoryModel) {
        editor.putString(REPOSITORY_MODEL_KEY, repositoryModel.toJson()).apply();
    }
}

