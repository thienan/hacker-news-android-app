package co.marcelje.hackernews.api;

import android.app.Activity;
import android.app.Application;

import co.marcelje.hackernews.HackerNewsApplication;
import co.marcelje.hackernews.loader.HackerNewsResponse;
import co.marcelje.hackernews.model.Item;
import co.marcelje.hackernews.model.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
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

    public Observable<Item> getItem(long itemId) {
        Timber.d("getItem(id=%d)", itemId);
        return resource.getItem(itemId);
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

    private <T> HackerNewsResponse<T> get(Observable<T> call) {
        final HackerNewsResponse<T>[] r = new HackerNewsResponse[1];

        call.subscribe(data -> {
            Timber.d(data.toString());
            r[0] = HackerNewsResponse.ok(data);
        }, throwable -> {
            Timber.e(throwable);
            r[0] = HackerNewsResponse.error(throwable.getMessage());
        });

        return r[0];
    }
}
