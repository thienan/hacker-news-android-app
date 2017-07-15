package com.marcelje.hackernews.screen.news.comment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.model.Item;

import org.parceler.Parcels;

public class CommentActivity extends AppCompatActivity {

    private static final String EXTRA_ITEM = "com.marcelje.hackernews.screen.news.comment.extra.ITEM";
    private static final String EXTRA_PARENT = "com.marcelje.hackernews.screen.news.comment.extra.PARENT";
    private static final String EXTRA_POSTER = "com.marcelje.hackernews.screen.news.comment.extra.POSTER";

    public static void startActivity(Activity activity, Item item, String parent, String poster) {
        Intent intent = new Intent(activity, CommentActivity.class);
        if (item != null) intent.putExtra(EXTRA_ITEM, Parcels.wrap(item));
        if (!TextUtils.isEmpty(parent)) intent.putExtra(EXTRA_PARENT, parent);
        if (!TextUtils.isEmpty(poster)) intent.putExtra(EXTRA_POSTER, poster);

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_left, R.anim.no_change);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.no_change, R.anim.slide_right);
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.no_change, R.anim.slide_right);
    }
}
