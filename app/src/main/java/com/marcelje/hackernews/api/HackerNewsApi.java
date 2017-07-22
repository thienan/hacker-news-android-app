package com.marcelje.hackernews.api;

import android.app.Activity;
import android.app.Application;

import com.marcelje.hackernews.HackerNewsApplication;
import com.marcelje.hackernews.loader.HackerNewsResponse;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.model.User;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

public final class HackerNewsApi {

    private static volatile HackerNewsApi instance;

    @Inject
    @SuppressWarnings("WeakerAccess")
    HackerNewsResource resource;

    public static HackerNewsApi with(Activity activity) {
        return with(activity.getApplication());
    }

    public static HackerNewsApi with(Application application) {
        HackerNewsApi result = instance;

        if (result == null) {
            synchronized (HackerNewsApi.class) {
                result = instance;

                if (result == null) {
                    instance = result = new HackerNewsApi(application);
                }
            }
        }

        return result;
    }

    private HackerNewsApi(Application application) {
        HackerNewsApplication.getApplication(application).getServiceComponent().inject(this);
    }

    public HackerNewsResponse<User> getUser(String userId) {
        Timber.d("getUser(id=%s)", userId);
        return get(resource.getUser(userId));
    }

    public HackerNewsResponse<Item> getItem(long itemId) {
        Timber.d("getItem(id=%d)", itemId);
        return get(resource.getItem(itemId));
    }

    public HackerNewsResponse<List<Long>> getTopStories() {
        Timber.d("getTopStories");
        return get(resource.getTopStories());
    }

    public HackerNewsResponse<List<Long>> getNewStories() {
        Timber.d("getNewStories");
        return get(resource.getNewStories());
    }

    public HackerNewsResponse<List<Long>> getBestStories() {
        Timber.d("getBestStories");
        return get(resource.getBestStories());
    }

    public HackerNewsResponse<List<Long>> getAskStories() {
        Timber.d("getAskStories");
        return get(resource.getAskStories());
    }

    public HackerNewsResponse<List<Long>> getShowStories() {
        Timber.d("getShowStories");
        return get(resource.getShowStories());
    }

    public HackerNewsResponse<List<Long>> getJobStories() {
        Timber.d("getJobStories");
        return get(resource.getJobStories());
    }

    private <T> HackerNewsResponse<T> get(Call<T> call) {
        try {
            Response<T> response = call.execute();

            if (response.isSuccessful()) {
                Timber.d(response.body().toString());
                return HackerNewsResponse.ok(response.body());
            } else {
                Timber.d(response.errorBody().toString());
                return HackerNewsResponse.error(response.errorBody().toString());
            }
        } catch (IOException e) {
            Timber.e(e);
            return HackerNewsResponse.error(e.getMessage());
        }
    }
}
