package com.marcelje.hackernews.model;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel(Parcel.Serialization.BEAN)
public class User {
    private String id = "";
    private int delay = 0;
    private long created = 0;
    private int karma = 0;
    private String about = "";
    private List<Long> submitted = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<Long> getSubmitted() {
        return submitted;
    }

    public void setSubmitted(List<Long> submitted) {
        this.submitted = submitted;
    }
}
