package com.marcelje.hackernews.screen.news.item;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.screen.news.item.head.StoryFragment;

public class StoryActivity extends BaseItemActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, StoryActivity.class);
    }

    public static void startActivity(ToolbarActivity activity, Item item, String parent, String poster) {
        Intent intent = new Intent(activity, StoryActivity.class);
        startActivity(activity, intent, item, parent, poster);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            ItemHeadFragment headFragment = StoryFragment.newInstance(mItem);
            ItemCommentFragment commentFragment = ItemCommentFragment.newInstance(mItem, mParent, mPoster);

            loadFragment(headFragment, commentFragment);
        }
    }
}
