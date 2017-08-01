package co.marcelje.hackernews.screen.about;

import android.content.Intent;
import android.os.Bundle;

import co.marcelje.hackernews.activity.FragmentActivity;
import co.marcelje.hackernews.activity.ToolbarActivity;

public class AboutActivity extends FragmentActivity {

    public static void startActivity(ToolbarActivity activity) {
        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            setFragment(AboutFragment.newInstance());
        }
    }

}
