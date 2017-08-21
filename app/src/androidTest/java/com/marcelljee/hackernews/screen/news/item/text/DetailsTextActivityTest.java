package com.marcelljee.hackernews.screen.news.item.text;

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
public class DetailsTextActivityTest {

    private static final String TEXT = "Test";

    @Rule
    public final ActivityTestRule<DetailsTextActivity> mActivityRule =
            new ActivityTestRule<>(DetailsTextActivity.class, false, false);

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra(DetailsTextActivity.EXTRA_TEXT, TEXT);
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void testShowDetailsText() {
        onView(withId(R.id.tv_news_details)).check(matches(withText(TEXT)));
    }

}