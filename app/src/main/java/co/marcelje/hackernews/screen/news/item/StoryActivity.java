package co.marcelje.hackernews.screen.news.item;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import co.marcelje.hackernews.activity.ToolbarActivity;
import co.marcelje.hackernews.model.Item;
import co.marcelje.hackernews.screen.news.item.comment.ItemCommentFragment;
import co.marcelje.hackernews.screen.news.item.head.ItemHeadFragment;
import co.marcelje.hackernews.screen.news.item.head.StoryFragment;

public class StoryActivity extends BaseItemActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, StoryActivity.class);
    }

    public static void startActivity(ToolbarActivity activity, Item item,
                                     String itemParentName, String itemPosterName) {
        Intent intent = new Intent(activity, StoryActivity.class);
        startActivity(activity, intent, item, itemParentName, itemPosterName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            ItemHeadFragment headFragment = StoryFragment.newInstance(getItem());
            ItemCommentFragment commentFragment = ItemCommentFragment
                    .newInstance(getItem(), getItemParentName(), getItemPosterName());

            loadFragment(headFragment, commentFragment);
        }
    }
}
