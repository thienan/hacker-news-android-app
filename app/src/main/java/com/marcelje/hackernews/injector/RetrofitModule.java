package com.marcelje.hackernews.injector;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.marcelje.hackernews.api.HackerNewsResource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
class RetrofitModule {

    private static final String BASE_URL = "https://hacker-news.firebaseio.com/v0/";

    @Provides
    @Singleton
    HackerNewsResource provideHackerNewsResource(Retrofit retrofit) {
        return retrofit.create(HackerNewsResource.class);
    }

    @Provides
    @Singleton
    Retrofit provideRetrovit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

}