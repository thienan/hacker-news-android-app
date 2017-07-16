package com.marcelje.hackernews.screen.news.comment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.utils.MenuUtils;

import org.parceler.Parcels;

public class CommentActivity extends ToolbarActivity {

    private static final String EXTRA_ITEM = "com.marcelje.hackernews.screen.news.comment.extra.ITEM";
    private static final String EXTRA_PARENT = "com.marcelje.hackernews.screen.news.comment.extra.PARENT";
    private static final String EXTRA_POSTER = "com.marcelje.hackernews.screen.news.comment.extra.POSTER";

    private Item mItem;
    private String mParent;
    private String mPoster;

    public static void startActivity(Activity activity, Item item, String parent, String poster) {
        Intent intent = new Intent(activity, CommentActivity.class);

        Bundle extras = createExtras(item, parent, poster);
        intent.putExtras(extras);

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_left, R.anim.no_change);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        setDisplayHomeAsUpEnabled(true);
        extractExtras();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.comment_container, CommentActivityFragment.newInstance(mItem, mParent, mPoster))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.no_change, R.anim.slide_right);
                return true;
            case R.id.action_share:
                MenuUtils.openShareHackerNewsLinkChooser(this, mItem);
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

    private static Bundle createExtras(Item item, String parent, String poster) {
        Bundle extras = new Bundle();
        if (item != null) extras.putParcelable(EXTRA_ITEM, Parcels.wrap(item));
        if (!TextUtils.isEmpty(parent)) extras.putString(EXTRA_PARENT, parent);
        if (!TextUtils.isEmpty(poster)) extras.putString(EXTRA_POSTER, poster);

        return extras;
    }

    private void extractExtras() {
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ITEM)) {
            mItem = Parcels.unwrap(intent.getParcelableExtra(EXTRA_ITEM));
        }

        if (intent.hasExtra(EXTRA_PARENT)) {
            mParent = intent.getStringExtra(EXTRA_PARENT);
        }

        if (intent.hasExtra(EXTRA_POSTER)) {
            mPoster = intent.getStringExtra(EXTRA_POSTER);
        }
    }
}
