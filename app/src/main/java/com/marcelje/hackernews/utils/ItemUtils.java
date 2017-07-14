package com.marcelje.hackernews.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.model.User;

public class ItemUtils {

    private static final String BRACKET_OPEN = " (";
    private static final String BRACKET_CLOSE = ")";

    public static CharSequence getRelativeDate(Context context, Item item) {
        if (context == null || item == null) return "";

        return DateUtils.getRelativeDateTimeString(context,
                item.getTime() * 1000,
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS, 0);
    }

    public static CharSequence getRelativeDate(Context context, User user) {
        if (context == null || user == null) return "";

        return DateUtils.getRelativeDateTimeString(context,
                user.getCreated() * 1000,
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.YEAR_IN_MILLIS, 0);
    }

    public static SpannableStringBuilder getTitle(Context context, Item item) {
        SpannableStringBuilder title = new SpannableStringBuilder();

        if (context == null || item == null) return title;

        title.append(item.getTitle());

        if (!TextUtils.isEmpty(item.getUrl())) {
            String host = Uri.parse(item.getUrl()).getHost();

            SpannableString url = new SpannableString(BRACKET_OPEN + host + BRACKET_CLOSE);
            url.setSpan(new ForegroundColorSpan(
                            ContextCompat.getColor(context, R.color.textColorSecondary)),
                    0, url.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            title.append(url);
        }

        return title;
    }

    public static SpannableStringBuilder getCommentInfo(String parent, String poster) {
        SpannableStringBuilder commentInfo = new SpannableStringBuilder();
        commentInfo.append("Replies to ");

        if (!TextUtils.isEmpty(parent)) {
            SpannableString comment = new SpannableString(String.format("%s's comment on ", parent));
            comment.setSpan(new StyleSpan(Typeface.BOLD), 0, parent.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            commentInfo.append(comment);
        }

        if (!TextUtils.isEmpty(poster)) {
            SpannableString post = new SpannableString(String.format("%s's post", poster));
            post.setSpan(new StyleSpan(Typeface.BOLD), 0, poster.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            commentInfo.append(post);
        }

        return commentInfo;
    }
}
