package com.marcelljee.hackernews.screen.settings;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.marcelljee.hackernews.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.core.AllOf.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.*;

@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest {

    @Rule
    public final ActivityTestRule<SettingsActivity> mActivityRule =
            new ActivityTestRule<>(SettingsActivity.class);

    @Test
    public void testChangeDefaultNews() {
        String bookmarked = mActivityRule.getActivity().getString(R.string.news_type_bookmarked);

        onView(withText(mActivityRule.getActivity().getString(R.string.settings_type_title)))
                .perform(click());

        onData(allOf(
                is(instanceOf(String.class)),
                is(bookmarked)
        ))
                .perform(click());

        onView(withText(bookmarked))
                .check(matches(isDisplayed()));
    }
}