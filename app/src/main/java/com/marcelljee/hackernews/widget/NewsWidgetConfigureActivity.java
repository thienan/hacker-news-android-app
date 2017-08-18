package com.marcelljee.hackernews.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.activity.ToolbarActivity;

public class NewsWidgetConfigureActivity extends ToolbarActivity {

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Spinner mNewsTypeSpinner;
    private Spinner mNewsCountSpinner;

    public static Intent createIntent(Context context, int appWidgetId) {
        Intent intent = new Intent(context, NewsWidgetConfigureActivity.class);

        Bundle extras = createExtras(appWidgetId);
        intent.putExtras(extras);

        return intent;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.widget_news_configure);

        extractExtras();

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        mNewsTypeSpinner = (Spinner) findViewById(R.id.news_type_spinner);
        mNewsCountSpinner = (Spinner) findViewById(R.id.news_count_spinner);

        mNewsTypeSpinner.setSelection((
                (ArrayAdapter<String>) mNewsTypeSpinner.getAdapter())
                .getPosition(NewsWidgetStorage.loadNewsType(this, mAppWidgetId)));

        mNewsCountSpinner.setSelection((
                (ArrayAdapter<String>) mNewsCountSpinner.getAdapter())
                .getPosition(String.valueOf(NewsWidgetStorage.loadNewsCount(this, mAppWidgetId))));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_widget_news_configure, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                String newsType = mNewsTypeSpinner.getSelectedItem().toString();
                int newsCount = Integer.parseInt(mNewsCountSpinner.getSelectedItem().toString());

                NewsWidgetStorage.saveWidgetConfig(this, mAppWidgetId, newsType, newsCount);

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                NewsWidgetUpdaterService.updateAppWidget(this, appWidgetManager, mAppWidgetId, newsType);

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(Activity.RESULT_OK, resultValue);
                finish();

                return true;
            default:
                return false;
        }
    }

    private static Bundle createExtras(int appWidgetId) {
        Bundle extras = new Bundle();
        extras.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        return extras;
    }

    private void extractExtras() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
    }
}

