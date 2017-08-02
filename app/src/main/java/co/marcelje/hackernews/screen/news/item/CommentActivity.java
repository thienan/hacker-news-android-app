package co.marcelje.hackernews.screen.news.item;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import co.marcelje.hackernews.activity.ToolbarActivity;
import co.marcelje.hackernews.model.Item;
import co.marcelje.hackernews.screen.news.NewsActivity;
import co.marcelje.hackernews.screen.news.item.comment.ItemCommentFragment;
import co.marcelje.hackernews.screen.news.item.head.CommentFragment;
import co.marcelje.hackernews.screen.news.item.head.ItemHeadFragment;

public class CommentActivity extends BaseItemActivity {

    public static void startActivity(ToolbarActivity activity, Item item, String parent, String poster) {
        Intent intent = new Intent(activity, CommentActivity.class);
        startActivity(activity, intent, item, parent, poster);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            ItemHeadFragment headFragment = CommentFragment
                    .newInstance(getItem(), getItemParentName(), getItemPosterName());
            ItemCommentFragment commentFragment = ItemCommentFragment
                    .newInstance(getItem(), getItemParentName(), getItemPosterName());

            loadFragment(headFragment, commentFragment);
        }
    }
}
