package com.marcelljee.hackernews.databinding.component;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.databinding.DataBindingComponent;
import android.view.View;
import android.widget.TextView;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.utils.ItemUtils;

public class HackerNewsDataBindingComponent implements DataBindingComponent {
    private final HackerNewsBindingAdapter mHackerNewsBindingAdapter;

    public HackerNewsDataBindingComponent(ToolbarActivity activity) {
        mHackerNewsBindingAdapter = new HackerNewsBindingAdapter(activity);
    }

    @Override
    public HackerNewsBindingAdapter getHackerNewsBindingAdapter() {
        return mHackerNewsBindingAdapter;
    }

    public static class HackerNewsBindingAdapter {
        private final ToolbarActivity mActivity;

        public HackerNewsBindingAdapter(ToolbarActivity activity) {
            mActivity = activity;
        }

        @BindingConversion
        public static int convertBooleanToVisibility(boolean isTrue) {
            return isTrue ? View.VISIBLE : View.GONE;
        }

        @BindingAdapter("bind:weekRelativeDate")
        public void getWeekDate(TextView textView, long timeInMillis) {
            textView.setText(ItemUtils.getWeekRelativeDate(mActivity, timeInMillis));
        }

        @BindingAdapter("bind:yearRelativeDate")
        public void getYearDate(TextView textView, long timeInMillis) {
            textView.setText(ItemUtils.getYearRelativeDate(mActivity, timeInMillis));
        }

        @BindingAdapter("bind:htmlText")
        public void getHtmlText(TextView textView, String text) {
            textView.setText(ItemUtils.fromHtml(mActivity, text));
        }

        @BindingAdapter("bind:title")
        public void getTitle(TextView textView, Item item) {
             textView.setText(ItemUtils.getTitle(mActivity, item.getTitle(), item.getUrl()));
        }
    }
}
