package com.marcelljee.hackernews.screen.about;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.utils.ItemUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class AboutActivityTest {

    @Rule
    public final ActivityTestRule<AboutActivity> mActivityRule = new ActivityTestRule(AboutActivity.class);

    @Test
    public void testShowAboutText() {
        onView(withId(R.id.tv_about)).check(matches(withText(ItemUtils.fromHtml(mActivityRule.getActivity(),
                mActivityRule.getActivity().getString(R.string.about_text)).toString())));

        onView(withId(R.id.tv_about)).check(matches(hasLinks()));
    }
}