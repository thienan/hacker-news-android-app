package com.marcelje.hackernews.factory;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.marcelje.hackernews.R;

public class SnackbarFactory {

    public static Snackbar createRetrieveErrorSnackbar(View view, View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(view, R.string.error_retrieve_data, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.action_retry, listener);

        return snackbar;
    }

}
