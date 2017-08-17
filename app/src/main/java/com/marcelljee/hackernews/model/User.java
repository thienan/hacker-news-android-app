package com.marcelljee.hackernews.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.marcelljee.hackernews.BR;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel(Parcel.Serialization.BEAN)
public class User extends BaseObservable {
    private String id = "";
    private int delay = 0;
    private long created = 0;
    private int karma = 0;
    private String about = "";
    private List<Long> submitted = new ArrayList<>();

    public static User createTempUser(String id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
        notifyPropertyChanged(BR.delay);
    }

    @Bindable
    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
        notifyPropertyChanged(BR.created);
    }

    @Bindable
    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
        notifyPropertyChanged(BR.karma);
    }

    @Bindable
    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
        notifyPropertyChanged(BR.about);
    }

    @Bindable
    public List<Long> getSubmitted() {
        return submitted;
    }

    public void setSubmitted(List<Long> submitted) {
        this.submitted = submitted;
        notifyPropertyChanged(BR.submitted);
    }

    public void update(User user) {
        if (!id.equals(user.getId())) return;
        if (delay != user.getDelay()) setDelay(user.getDelay());
        if (created != user.getCreated()) setCreated(user.getCreated());
        if (karma != user.getKarma()) setKarma(user.getKarma());
        if (!about.equals(user.getAbout())) setAbout(user.getAbout());
        if (!submitted.equals(user.getSubmitted())) setSubmitted(user.getSubmitted());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", delay=" + delay +
                ", created=" + created +
                ", karma=" + karma +
                ", about='" + about + '\'' +
                ", submitted=" + submitted +
                '}';
    }
}
