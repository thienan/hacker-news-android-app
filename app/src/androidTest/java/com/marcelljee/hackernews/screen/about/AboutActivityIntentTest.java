package com.marcelljee.hackernews.screen.about;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.chrome.CustomTabsBrowser;
import com.marcelljee.hackernews.screen.web.WebActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.intent.Intents.*;
import static android.support.test.espresso.intent.matcher.IntentMatchers.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.core.AllOf.*;

@RunWith(AndroidJUnit4.class)
public class AboutActivityIntentTest {

    @Rule
    public final IntentsTestRule<AboutActivity> intentsTestRule = new IntentsTestRule<>(AboutActivity.class);

    @Test
    public void testClickLink() {
        onView(withId(R.id.tv_about)).perform(openLinkWithText("Github page"));

        String packageName = CustomTabsBrowser.getCustomTabPackageName(intentsTestRule.getActivity());

        if (packageName == null) {
            intended(allOf(
                    hasComponent(WebActivity.class.getName()),
                    hasExtra(WebActivity.EXTRA_URL, "https://github.com/marcelljee/hacker-news-android-app")
            ));
        } else {
            intended(allOf(
                    hasAction(Intent.ACTION_VIEW),
                    hasData("https://github.com/marcelljee/hacker-news-android-app"),
                    toPackage(packageName)
            ));
        }
    }
}