package com.marcelje.hackernews.screen.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.marcelje.hackernews.R;

public class UserActivity extends AppCompatActivity {

    private static final String EXTRA_USER_ID = "com.marcelje.hackernews.screen.user.extra.USER_ID";

    public static void startActivity(Context context, String userId) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        context.startActivity(intent);
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

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_USER_ID)) {
            String userId = intent.getStringExtra(EXTRA_USER_ID);

            setTitle(String.format(getString(R.string.title_profile), userId));

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.user_container, UserActivityFragment.newInstance(userId))
                    .commit();
        }
    }

}
