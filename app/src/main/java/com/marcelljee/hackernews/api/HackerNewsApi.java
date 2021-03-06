package com.marcelljee.hackernews.api;

import com.marcelljee.hackernews.HackerNewsApplication;
import com.marcelljee.hackernews.loader.AppResponse;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.model.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import timber.log.Timber;

public final class HackerNewsApi {

    private static volatile HackerNewsApi instance;

    @Inject
    @SuppressWarnings("WeakerAccess")
    HackerNewsResource resource;

    public static HackerNewsApi getInstance() {
        HackerNewsApi result = instance;

        if (result == null) {
            synchronized (HackerNewsApi.class) {
                result = instance;

                if (result == null) {
                    instance = result = new HackerNewsApi();
                }
            }
        }

        return result;
    }

    private HackerNewsApi() {
        HackerNewsApplication.getInstance().getServiceComponent().inject(this);
    }

    public AppResponse<User> getUser(String userId) {
        Timber.d("getUser(id=%s)", userId);
        return get(resource.getUser(userId));
    }

    public Observable<Item> getItem(long itemId) {
        Timber.d("getItem(id=%d)", itemId);
        return resource.getItem(itemId);
    }

    public AppResponse<List<Long>> getTopStories() {
        Timber.d("getTopStories");
        return get(resource.getTopStories());
    }

    public AppResponse<List<Long>> getNewStories() {
        Timber.d("getNewStories");
        return get(resource.getNewStories());
    }

    public AppResponse<List<Long>> getBestStories() {
        Timber.d("getBestStories");
        return get(resource.getBestStories());
    }

    public AppResponse<List<Long>> getAskStories() {
        Timber.d("getAskStories");
        return get(resource.getAskStories());
    }

    public AppResponse<List<Long>> getShowStories() {
        Timber.d("getShowStories");
        return get(resource.getShowStories());
    }

    public AppResponse<List<Long>> getJobStories() {
        Timber.d("getJobStories");
        return get(resource.getJobStories());
    }

    private <T> AppResponse<T> get(Observable<T> call) {
        final AppResponse<T>[] r = new AppResponse[1];

        call.subscribe(data -> {
            Timber.d(data.toString());
            r[0] = AppResponse.ok(data);
        }, throwable -> {
            Timber.e(throwable);
            r[0] = AppResponse.error(throwable.getMessage());
        });

        return r[0];
    }
}
