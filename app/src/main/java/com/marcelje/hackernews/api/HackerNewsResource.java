package com.marcelje.hackernews.api;

import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.model.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HackerNewsResource {

    @GET("user/{id}.json")
    Observable<User> getUser(@Path("id") String id);

    @GET("item/{id}.json")
    Observable<Item> getItem(@Path("id") long id);

    @GET("topstories.json")
    Observable<List<Long>> getTopStories();

    @GET("newstories.json")
    Observable<List<Long>> getNewStories();

    @GET("beststories.json")
    Observable<List<Long>> getBestStories();

    @GET("askstories.json")
    Observable<List<Long>> getAskStories();

    @GET("showstories.json")
    Observable<List<Long>> getShowStories();

    @GET("jobstories.json")
    Observable<List<Long>> getJobStories();
}
