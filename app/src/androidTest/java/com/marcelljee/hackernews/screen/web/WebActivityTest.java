package com.marcelljee.hackernews.screen.web;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.marcelljee.hackernews.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class WebActivityTest {

    private static final String URL = "https://github.com/marcelljee/hacker-news-android-app";

    private Context mContext = InstrumentationRegistry.getTargetContext();

    @Rule
    public final IntentsTestRule<WebActivity> intentsTestRule =
            new IntentsTestRule<>(WebActivity.class, false, false);

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra(WebActivity.EXTRA_URL, URL);
        intentsTestRule.launchActivity(intent);
    }

    @Test
    public void testToolbarActionMenu_clickShareMenuItem() {
        openActionBarOverflowOrOptionsMenu(mContext);
        onView(withText(intentsTestRule.getActivity().getString(R.string.menu_item_share)))
                .perform(click());

        intended(allOf(
                hasAction(Intent.ACTION_CHOOSER),
                hasExtra(Intent.EXTRA_TITLE, intentsTestRule.getActivity().getString(R.string.title_share)),
                hasExtraWithKey(Intent.EXTRA_INTENT)
        ));
    }

    @Test
    public void testToolbarActionMenu_clickOpenInBrowserMenuItem() {
        openActionBarOverflowOrOptionsMenu(mContext);
        onView(withText(intentsTestRule.getActivity().getString(R.string.menu_item_open_in_browser)))
                .perform(click());

        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(URL)
        ));
    }
}