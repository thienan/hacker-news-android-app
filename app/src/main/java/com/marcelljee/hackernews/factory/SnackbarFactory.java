package com.marcelljee.hackernews.factory;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.marcelljee.hackernews.R;

@SuppressWarnings("WeakerAccess")
public class SnackbarFactory {

    public static Snackbar createRetrieveErrorSnackbar(View view) {
        Snackbar snackbar = Snackbar.make(view, R.string.message_error_retrieve_data, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.action_dismiss, v -> snackbar.dismiss());
        return snackbar;
    }
}
