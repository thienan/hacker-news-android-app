package com.marcelljee.hackernews.screen.news;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.screen.about.AboutActivity;
import com.marcelljee.hackernews.screen.espresso.CustomViewActions;
import com.marcelljee.hackernews.screen.settings.SettingsActivity;
import com.marcelljee.hackernews.utils.SettingsUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.*;

@RunWith(AndroidJUnit4.class)
public class NewsActivityTest {

    private Context mContext;

    @Rule
    public final IntentsTestRule<NewsActivity> intentsTestRule =
            new IntentsTestRule<>(NewsActivity.class);

    @Before
    public void setUp() {
        mContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void testToolbarActionMenu_checkClearHistoryMenuItemIsShown() {
        onView(withId(R.id.spinner_news_type)).perform(click());
        onData(allOf(
                is(instanceOf(String.class)),
                is(mContext.getString(R.string.news_type_history))
        )).perform(click());
        onView(withId(R.id.action_clear_history)).check(matches(isDisplayed()));

        onView(withId(R.id.spinner_news_type)).perform(click());
        onData(allOf(
                is(instanceOf(String.class)),
                is(mContext.getString(R.string.news_type_top))
        )).perform(click());
        onView(withId(R.id.action_clear_history)).check(doesNotExist());
    }

    @Test
    public void testToolbarActionMenu_checkShowMenuItemIsShown() {
        onView(withId(R.id.spinner_news_type)).perform(click());
        onData(allOf(
                is(instanceOf(String.class)),
                is(mContext.getString(R.string.news_type_history))
        )).perform(click());

        onView(withId(R.id.action_show_all)).check(doesNotExist());
        onView(withId(R.id.action_show_unread)).check(doesNotExist());

        onView(withId(R.id.spinner_news_type)).perform(click());
        onData(allOf(
                is(instanceOf(String.class)),
                is(mContext.getString(R.string.news_type_top))
        )).perform(click());

        if (SettingsUtils.readIndicatorEnabled(mContext)) {
            onView(withId(R.id.action_show_all)).check(doesNotExist());
            onView(withId(R.id.action_show_unread)).check(matches(isDisplayed()));

            onView(withId(R.id.action_show_unread)).perform(click());

            onView(withId(R.id.action_show_all)).check(matches(isDisplayed()));
            onView(withId(R.id.action_show_unread)).check(doesNotExist());

            onView(withId(R.id.action_show_all)).perform(click());

            onView(withId(R.id.action_show_all)).check(doesNotExist());
            onView(withId(R.id.action_show_unread)).check(matches(isDisplayed()));
        } else {
            onView(withId(R.id.action_show_all)).check(doesNotExist());
            onView(withId(R.id.action_show_unread)).check(doesNotExist());
        }
    }

    @Test
    public void testToolbarActionMenu_clickAboutMenuItem() {
        openActionBarOverflowOrOptionsMenu(mContext);
        onView(withText(mContext.getString(R.string.menu_item_about)))
                .perform(click());

        intended(hasComponent(AboutActivity.class.getName()));
    }

    @Test
    public void testToolbarActionMenu_clickSettingsMenuItem() {
        openActionBarOverflowOrOptionsMenu(mContext);
        onView(withText(mContext.getString(R.string.menu_item_settings)))
                .perform(click());

        intended(hasComponent(SettingsActivity.class.getName()));
    }

    @Test
    public void testShareItem_onClickScoreView() {
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        CustomViewActions.clickChildViewWithId(R.id.sv_score)));

        onView(withId(R.id.action_share)).perform(click());

        intended(allOf(
                hasAction(Intent.ACTION_CHOOSER),
                hasExtra(Intent.EXTRA_TITLE, mContext.getString(R.string.title_share)),
                hasExtraWithKey(Intent.EXTRA_INTENT)
        ));
    }
}