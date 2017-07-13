package com.marcelje.hackernews.model;

import lombok.Data;

@Data
public class Item {

    private long id;
    private boolean deleted;
    private String type;
    private String by;
    private long time;
    private String text;
    private boolean dead;
    private long parent;
    private long poll;
    private long[] kids;
    private String url;
    private int score;
    private String title;
    private long[] parts;
    private int descendants;
}
