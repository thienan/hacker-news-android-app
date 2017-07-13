package com.marcelje.hackernews.api;

import com.marcelje.hackernews.model.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HackerNewsResource {

    @GET("item/{id}.json")
    Call<Item> getItem(@Path("id") long id);

    @GET("topstories.json")
    Call<List<Long>> getTopStories();

    @GET("newstories.json")
    Call<List<Long>> getNewStories();

    @GET("beststories.json")
    Call<List<Long>> getBestStories();

    @GET("askstories.json")
    Call<List<Long>> getAskStories();

    @GET("showstories.json")
    Call<List<Long>> getShowStories();

    @GET("jobstories.json")
    Call<List<Long>> getJobStories();
}
