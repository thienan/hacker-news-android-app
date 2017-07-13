package com.marcelje.hackernews.api;

import android.app.Activity;

import com.marcelje.hackernews.HackerNewsApplication;
import com.marcelje.hackernews.model.Item;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public final class HackerNewsApi {

    private static volatile HackerNewsApi instance;

    @Inject
    @SuppressWarnings("WeakerAccess")
    HackerNewsResource resource;

    public interface RestCallback<T> {
        void onSuccess(T data);

        void onFailure(String message);
    }

    private HackerNewsApi(Activity activity) {
        HackerNewsApplication.getApplication(activity).getServiceComponent().inject(this);
    }

    public static HackerNewsApi with(Activity activity) {
        HackerNewsApi result = instance;

        if (result == null) {
            synchronized (HackerNewsApi.class) {
                result = instance;

                if (result == null) {
                    instance = result = new HackerNewsApi(activity);
                }
            }
        }

        return result;
    }

    public void getItem(long id, final RestCallback<Item> callback) {
        Timber.d("getItem(id=%d)", id);
        resource.getItem(id).enqueue(getCallback(callback));
    }

    public void getTopStories(final RestCallback<List<Long>> callback) {
        Timber.d("getTopStories");
        resource.getTopStories().enqueue(getCallback(callback));
    }

    public void getNewStories(final RestCallback<List<Long>> callback) {
        Timber.d("getNewStories");
        resource.getNewStories().enqueue(getCallback(callback));
    }

    public void getBestStories(final RestCallback<List<Long>> callback) {
        Timber.d("getBestStories");
        resource.getBestStories().enqueue(getCallback(callback));
    }

    public void getAskStories(final RestCallback<List<Long>> callback) {
        Timber.d("getAskStories");
        resource.getAskStories().enqueue(getCallback(callback));
    }

    public void getShowStories(final RestCallback<List<Long>> callback) {
        Timber.d("getShowStories");
        resource.getShowStories().enqueue(getCallback(callback));
    }

    public void getJobStories(final RestCallback<List<Long>> callback) {
        Timber.d("getJobStories");
        resource.getJobStories().enqueue(getCallback(callback));
    }

    private <T> Callback<T> getCallback(final RestCallback<T> callback) {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                Timber.d(response.raw().toString());

                if (response.isSuccessful()) {
                    Timber.d(response.body().toString());
                    callback.onSuccess(response.body());
                } else {
                    try {
                        Timber.d(response.errorBody().toString());
                        callback.onFailure(response.errorBody().string());
                    } catch (IOException e) {
                        Timber.e(e);
                        callback.onFailure(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Timber.e(t);
                callback.onFailure(t.getMessage());
            }
        };
    }
}
