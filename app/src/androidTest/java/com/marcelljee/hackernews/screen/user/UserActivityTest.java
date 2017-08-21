package com.marcelljee.hackernews.screen.user;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.marcelljee.hackernews.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class UserActivityTest {

    private static final String USER_ID = "zserge";

    @Rule
    public final ActivityTestRule<UserActivity> mActivityRule =
            new ActivityTestRule<>(UserActivity.class, false, false);

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra(UserActivity.EXTRA_USER_ID, USER_ID);
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void testShowUserInfo() {
        onView(withText(mActivityRule.getActivity().getString(R.string.title_activity_user, USER_ID)))
                .check(matches(isDisplayed()));
        onView(withId(R.id.tv_user_id)).check(matches(withText(USER_ID)));
    }

}