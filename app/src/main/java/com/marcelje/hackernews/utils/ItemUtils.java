package com.marcelje.hackernews.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.activity.ToolbarActivity;
import com.marcelje.hackernews.model.Item;
import com.marcelje.hackernews.model.User;
import com.marcelje.hackernews.span.CustomTabUrlSpan;
import com.marcelje.hackernews.span.UserClickableSpan;

@SuppressWarnings("WeakerAccess")
public final class ItemUtils {

    private static final String BRACKET_OPEN = " (";
    private static final String BRACKET_CLOSE = ")";

    private ItemUtils() {
    }

    public static String getTypeAsTitle(Item item) {
        if (item == null) return "";

        String type = item.getType();
        return String.valueOf(type.charAt(0)).toUpperCase() + type.substring(1).toLowerCase();
    }

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

    public static SpannableStringBuilder getCommentInfo(final ToolbarActivity activity, final String parent, final String poster) {
        SpannableStringBuilder commentInfo = new SpannableStringBuilder();
        if (TextUtils.isEmpty(parent) && TextUtils.isEmpty(poster)) return commentInfo;
        commentInfo.append("Reply of ");

        if (!TextUtils.isEmpty(parent)) {
            SpannableString comment = new SpannableString(String.format("%s's comment on ", parent));
            comment.setSpan(new StyleSpan(Typeface.BOLD), 0, parent.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            comment.setSpan(new UserClickableSpan(activity, parent), 0, parent.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            commentInfo.append(comment);
        }

        if (!TextUtils.isEmpty(poster)) {
            SpannableString post = new SpannableString(String.format("%s's post", poster));
            post.setSpan(new StyleSpan(Typeface.BOLD), 0, poster.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            post.setSpan(new UserClickableSpan(activity, poster), 0, poster.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            commentInfo.append(post);
        }

        return commentInfo;
    }

    public static SpannableStringBuilder fromHtml(ToolbarActivity activity, String text) {
        SpannableStringBuilder spannedText = new SpannableStringBuilder();
        if (TextUtils.isEmpty(text)) return spannedText;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spannedText = (SpannableStringBuilder) Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            //noinspection deprecation
            spannedText = (SpannableStringBuilder) Html.fromHtml(text);
        }

        URLSpan[] spans = spannedText.getSpans(0, spannedText.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = spannedText.getSpanStart(span);
            int end = spannedText.getSpanEnd(span);
            spannedText.removeSpan(span);
            span = new CustomTabUrlSpan(activity, span.getURL());
            spannedText.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }

        return spannedText;
    }
}
