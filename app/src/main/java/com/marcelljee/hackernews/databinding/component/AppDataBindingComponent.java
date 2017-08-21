package com.marcelljee.hackernews.databinding.component;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.databinding.DataBindingComponent;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.utils.ItemUtils;

public class AppDataBindingComponent implements DataBindingComponent {
    private final AppBindingAdapter mAppBindingAdapter;

    public AppDataBindingComponent(ToolbarActivity activity) {
        mAppBindingAdapter = new AppBindingAdapter(activity);
    }

    @Override
    public AppBindingAdapter getAppBindingAdapter() {
        return mAppBindingAdapter;
    }

    public static class AppBindingAdapter {
        private final ToolbarActivity mActivity;

        public AppBindingAdapter(ToolbarActivity activity) {
            mActivity = activity;
        }

        @BindingConversion
        public static int convertBooleanToVisibility(boolean isTrue) {
            return isTrue ? View.VISIBLE : View.GONE;
        }

        @BindingAdapter("bind:weekRelativeDate")
        public void setWeekRelativeDate(TextView textView, long timeInMillis) {
            textView.setText(ItemUtils.getWeekRelativeDate(mActivity, timeInMillis));
        }

        @BindingAdapter("bind:yearRelativeDate")
        public void setYearRelativeDate(TextView textView, long timeInMillis) {
            textView.setText(ItemUtils.getYearRelativeDate(mActivity, timeInMillis));
        }

        @BindingAdapter("bind:htmlText")
        public void setHtmlText(TextView textView, String text) {
            textView.setText(ItemUtils.fromHtml(mActivity, text));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }

        @BindingAdapter("bind:title")
        public void setTitle(TextView textView, Item item) {
            String title = item != null ? item.getTitle() : "";
            String url = item != null ? item.getUrl() : "";

            textView.setText(ItemUtils.getTitle(mActivity, title, url));
        }

        @BindingAdapter("bind:selected")
        public void setSelected(View view, boolean selected) {
            view.setSelected(selected);
        }
    }
}
