package com.marcelje.hackernews.utils;

import android.content.Context;
import android.content.Intent;

import com.marcelje.hackernews.R;
import com.marcelje.hackernews.model.Item;

public class MenuUtils {

    public static void openShareChooser(Context context, Item item) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String text = String.format(context.getString(R.string.url_hackernews_item), item.getId());
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");

        context.startActivity(Intent.createChooser(intent, context.getString(R.string.title_share)));
    }
}
