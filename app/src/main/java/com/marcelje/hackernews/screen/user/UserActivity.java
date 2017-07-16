package com.marcelje.hackernews.screen.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.marcelje.hackernews.R;

public class UserActivity extends AppCompatActivity {

    private static final String EXTRA_USER_ID = "com.marcelje.hackernews.screen.user.extra.USER_ID";

    private String mUserId;

    public static void startActivity(Activity activity, String userId) {
        Intent intent = new Intent(activity, UserActivity.class);

        Bundle extras = createExtras(userId);
        intent.putExtras(extras);

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_left, R.anim.no_change);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        extractExtras();

        setTitle(String.format(getString(R.string.title_profile), mUserId));

        getSupportFragmentManager().beginTransaction()
                .add(R.id.user_container, UserActivityFragment.newInstance(mUserId))
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

    private static Bundle createExtras(String userId) {
        Bundle extras = new Bundle();
        extras.putString(EXTRA_USER_ID, userId);

        return extras;
    }

    private void extractExtras() {
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_USER_ID)) {
            mUserId = intent.getStringExtra(EXTRA_USER_ID);
        }
    }
}
