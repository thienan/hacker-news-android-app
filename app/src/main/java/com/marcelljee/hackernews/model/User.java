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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (delay != user.delay) return false;
        if (created != user.created) return false;
        if (karma != user.karma) return false;
        if (!id.equals(user.id)) return false;
        if (about != null ? !about.equals(user.about) : user.about != null) return false;
        return submitted != null ? submitted.equals(user.submitted) : user.submitted == null;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + delay;
        result = 31 * result + (int) (created ^ (created >>> 32));
        result = 31 * result + karma;
        result = 31 * result + (about != null ? about.hashCode() : 0);
        result = 31 * result + (submitted != null ? submitted.hashCode() : 0);
        return result;
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
