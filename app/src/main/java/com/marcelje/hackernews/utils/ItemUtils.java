package com.marcelje.hackernews.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.model.Item;

public class ItemUtils {

    private static final String BRACKET_OPEN = " (";
    private static final String BRACKET_CLOSE = ")";

    public static CharSequence getRelativeDate(Context context, Item item) {
        return DateUtils.getRelativeDateTimeString(context,
                item.getTime() * 1000,
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS, 0);
    }

    public static SpannableStringBuilder getTitle(Context context, Item item) {
        SpannableStringBuilder title = new SpannableStringBuilder();

        title.append(item.getTitle());

        if (!TextUtils.isEmpty(item.getUrl())) {
            String url = Uri.parse(item.getUrl()).getHost();
            title.append(BRACKET_OPEN).append(url).append(BRACKET_CLOSE);
            title.setSpan(
                    new ForegroundColorSpan(
                            ContextCompat.getColor(context, R.color.textColorSecondary)),
                    item.getTitle().length(), title.length(), 0);
        }

        return title;
    }

}
