package com.marcelljee.hackernews.utils;

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

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.activity.ToolbarActivity;
import com.marcelljee.hackernews.model.Item;
import com.marcelljee.hackernews.span.CustomTabUrlSpan;
import com.marcelljee.hackernews.span.UserClickableSpan;

@SuppressWarnings("WeakerAccess")
public final class ItemUtils {

    private static final String BRACKET_OPEN = " (";
    private static final String BRACKET_CLOSE = ")";

    private ItemUtils() {}

    public static String getTypeAsTitle(Item item) {
        if (item == null) return "";

        String type = item.getType();
        return String.valueOf(type.charAt(0)).toUpperCase() + type.substring(1).toLowerCase();
    }

    public static CharSequence getRelativeDate(Context context, Item item) {
        if (context == null || item == null) return "";
        return getWeekRelativeDate(context, item.getTime());
    }

    public static CharSequence getWeekRelativeDate(Context context, long timeInMillis) {
        return getRelativeDate(context, timeInMillis, DateUtils.WEEK_IN_MILLIS);
    }

    public static CharSequence getYearRelativeDate(Context context, long timeInMillis) {
        return getRelativeDate(context, timeInMillis, DateUtils.YEAR_IN_MILLIS);
    }

    public static CharSequence getRelativeDate(Context context, long timeInMillis, long transitionResolution) {
        return DateUtils.getRelativeDateTimeString(context,
                timeInMillis * DateUtils.SECOND_IN_MILLIS,
                DateUtils.SECOND_IN_MILLIS,
                transitionResolution, 0);
    }

    public static SpannableStringBuilder getTitle(Context context, String text, String link) {
        SpannableStringBuilder title = new SpannableStringBuilder();
        if (context == null) return title;
        title.append(text);

        if (!TextUtils.isEmpty(link)) {
            String host = Uri.parse(link).getHost();

            SpannableString url = new SpannableString(BRACKET_OPEN + host + BRACKET_CLOSE);
            url.setSpan(new ForegroundColorSpan(
                            ContextCompat.getColor(context, R.color.textColorSecondary)),
                    0, url.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            title.append(url);
        }

        return title;
    }

    public static SpannableStringBuilder getCommentInfo(final ToolbarActivity activity,
                                                        final String itemParentName, final String itemPosterName) {
        SpannableStringBuilder commentInfo = new SpannableStringBuilder();
        if (TextUtils.isEmpty(itemParentName) && TextUtils.isEmpty(itemPosterName)) return commentInfo;
        commentInfo.append("Reply of ");

        if (!TextUtils.isEmpty(itemParentName)) {
            SpannableString comment = new SpannableString(String.format("%s's comment on ", itemParentName));
            comment.setSpan(new StyleSpan(Typeface.BOLD),
                    0, itemParentName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            comment.setSpan(new UserClickableSpan(activity, itemParentName),
                    0, itemParentName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            commentInfo.append(comment);
        }

        if (!TextUtils.isEmpty(itemPosterName)) {
            SpannableString post = new SpannableString(String.format("%s's story", itemPosterName));
            post.setSpan(new StyleSpan(Typeface.BOLD),
                    0, itemPosterName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            post.setSpan(new UserClickableSpan(activity, itemPosterName),
                    0, itemPosterName.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
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
