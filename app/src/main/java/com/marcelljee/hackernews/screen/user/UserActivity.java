package com.marcelljee.hackernews.screen.user;

import android.content.Intent;
import android.os.Bundle;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.activity.FragmentActivity;

public class UserActivity extends FragmentActivity<UserFragment> {

    private static final String EXTRA_USER_ID = "com.marcelljee.hackernews.screen.user.extra.USER_ID";

    private String mUserId;

    public static void startActivity(ToolbarActivity activity, String userId) {
        Intent intent = new Intent(activity, UserActivity.class);

        Bundle extras = createExtras(userId);
        intent.putExtras(extras);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(true);

        extractExtras();

        setTitle(String.format(getString(R.string.title_activity_user), mUserId));

        if (savedInstanceState == null) {
            setFragment(UserFragment.newInstance(mUserId));
        }
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
