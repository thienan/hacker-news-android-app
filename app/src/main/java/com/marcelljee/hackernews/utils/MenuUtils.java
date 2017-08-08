package com.marcelljee.hackernews.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.model.Item;

public final class MenuUtils {

    private static final String MIME_TEXT = "text/plain";

    private MenuUtils() {}

    public static void openShareHackerNewsLinkChooser(Context context, Item item) {
        if (context == null || item == null) return;

        Intent intent = new Intent(Intent.ACTION_SEND);
        String text = HackerNewsUtils.geItemUrl(item.getId());
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType(MIME_TEXT);

        Intent chooserIntent = Intent.createChooser(intent, context.getString(R.string.title_share));
        chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(chooserIntent);
    }

    public static void openShareTextChooser(Context context, String text) {
        if (context == null || TextUtils.isEmpty(text)) return;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType(MIME_TEXT);

        Intent chooserIntent = Intent.createChooser(intent, context.getString(R.string.title_share));
        chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(chooserIntent);
    }
}
