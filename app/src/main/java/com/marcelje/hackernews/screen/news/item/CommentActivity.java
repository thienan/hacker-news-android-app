package com.marcelje.hackernews.screen.news.item;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.screen.news.item.comment.ItemCommentFragment;
import com.marcelje.hackernews.screen.news.item.head.CommentFragment;
import com.marcelje.hackernews.screen.news.item.head.ItemHeadFragment;

public class CommentActivity extends BaseItemActivity {

    public static void startActivity(ToolbarActivity activity, Item item, String parent, String poster) {
        Intent intent = new Intent(activity, CommentActivity.class);
        startActivity(activity, intent, item, parent, poster);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            ItemHeadFragment headFragment = CommentFragment.newInstance(mItem, mParent, mPoster);
            ItemCommentFragment commentFragment = ItemCommentFragment.newInstance(mItem, mParent, mPoster);

            loadFragment(headFragment, commentFragment);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (!StoryActivity.class.getName().equals(mCallerActivity)) {
                    if (mParentItem != null) {
                        if (ITEM_TYPE_COMMENT.equals(mParentItem.getType())) {
                            CommentActivity.startActivity(this, mParentItem);
                        } else {
                            StoryActivity.startActivity(this, mParentItem);
                        }
                    } else {
                        loadParentItem();
                    }

                    return true;
                }

                //fall through
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
