package com.marcelje.hackernews.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.model.Item;

public final class MenuUtils {

    private MenuUtils() {}

    public static void openShareHackerNewsLinkChooser(Context context, Item item) {
        if (context == null || item == null) return;

        Intent intent = new Intent(Intent.ACTION_SEND);
        String text = String.format(context.getString(R.string.url_hackernews_item), item.getId());
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");

        Intent chooserIntent = Intent.createChooser(intent, context.getString(R.string.title_share));
        chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(chooserIntent);
    }

    public static void openShareTextChooser(Context context, String text) {
        if (context == null || TextUtils.isEmpty(text)) return;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");

        Intent chooserIntent = Intent.createChooser(intent, context.getString(R.string.title_share));
        chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(chooserIntent);
    }
}
