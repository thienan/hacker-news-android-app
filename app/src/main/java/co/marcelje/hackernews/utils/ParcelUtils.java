package co.marcelje.hackernews.utils;

import android.content.ContentValues;
import android.os.Parcelable;

public final class ParcelUtils {

    private ParcelUtils() {
    }

    public static ContentValues[] unwrapToContentValues(Parcelable[] input) {
        ContentValues[] values = new ContentValues[input.length];
        System.arraycopy(input, 0, values, 0, input.length);

        return values;
    }
}
