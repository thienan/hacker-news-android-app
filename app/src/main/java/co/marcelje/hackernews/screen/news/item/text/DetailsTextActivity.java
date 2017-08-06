package co.marcelje.hackernews.screen.news.item.text;

import android.content.Intent;
import android.os.Bundle;

import co.marcelje.hackernews.activity.ToolbarActivity;
import co.marcelje.hackernews.activity.FragmentActivity;

public class DetailsTextActivity extends FragmentActivity {

    private static final String EXTRA_TEXT = "co.marcelje.hackernews.screen.news.item.text.extra.TEXT";

    private String mText;

    public static void startActivity(ToolbarActivity activity, String text) {
        Intent intent = new Intent(activity, DetailsTextActivity.class);

        Bundle extras = createExtras(text);
        intent.putExtras(extras);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(true);

        extractExtras();

        if (savedInstanceState == null) {
            setFragment(DetailsTextFragment.newInstance(mText));
        }
    }

    private static Bundle createExtras(String text) {
        Bundle extras = new Bundle();
        extras.putString(EXTRA_TEXT, text);

        return extras;
    }

    private void extractExtras() {
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_TEXT)) {
            mText = intent.getStringExtra(EXTRA_TEXT);
        }
    }
}