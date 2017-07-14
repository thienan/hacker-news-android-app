package com.marcelje.hackernews.screen.news.comment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.model.Item;

import org.parceler.Parcels;

public class CommentActivity extends AppCompatActivity {

    private static final String EXTRA_ITEM = "com.marcelje.hackernews.screen.news.comment.extra.ITEM";
    private static final String EXTRA_PARENT = "com.marcelje.hackernews.screen.news.comment.extra.PARENT";
    private static final String EXTRA_POSTER = "com.marcelje.hackernews.screen.news.comment.extra.POSTER";

    public static void startActivity(Context context, Item item, String parent, String poster) {
        Intent intent = new Intent(context, CommentActivity.class);
        if (item != null) intent.putExtra(EXTRA_ITEM, Parcels.wrap(item));
        if (!TextUtils.isEmpty(parent)) intent.putExtra(EXTRA_PARENT, parent);
        if (!TextUtils.isEmpty(poster)) intent.putExtra(EXTRA_POSTER, poster);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Item item = null;
        String parent = null;
        String poster = null;

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ITEM)) {
            item = Parcels.unwrap(intent.getParcelableExtra(EXTRA_ITEM));
        }

        if (intent.hasExtra(EXTRA_PARENT)) {
            parent = intent.getStringExtra(EXTRA_PARENT);
        }

        if (intent.hasExtra(EXTRA_POSTER)) {
            poster = intent.getStringExtra(EXTRA_POSTER);
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.comment_container, CommentActivityFragment.newInstance(item, parent, poster))
                .commit();
    }

}
