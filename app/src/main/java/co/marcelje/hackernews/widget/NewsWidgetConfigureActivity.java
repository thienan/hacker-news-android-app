package co.marcelje.hackernews.widget;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.marcelje.hackernews.R;
import co.marcelje.hackernews.activity.ToolbarActivity;
import co.marcelje.hackernews.utils.MenuUtils;
import co.marcelje.hackernews.utils.SettingsUtils;

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

        setUpNewsTypeSpinner();
        setUpNewsCountSpinner();
    }

    private void setUpNewsTypeSpinner() {
        mNewsTypeSpinner = (Spinner) findViewById(R.id.news_type_spinner);
        List<CharSequence> newsTypeList =
                new ArrayList<>(Arrays.asList(getResources().getTextArray(R.array.settings_type_options)));
        ArrayAdapter<CharSequence> newsTypeAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, newsTypeList);
        newsTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNewsTypeSpinner.setAdapter(newsTypeAdapter);
        mNewsTypeSpinner.setSelection(newsTypeAdapter
                .getPosition(NewsWidgetStorage.loadNewsType(this, mAppWidgetId)));

        newsTypeAdapter.remove(getString(R.string.settings_type_option_history));
    }

    private void setUpNewsCountSpinner() {
        mNewsCountSpinner = (Spinner) findViewById(R.id.news_count_spinner);
        List<CharSequence> newsCountList =
                new ArrayList<>(Arrays.asList(getResources().getTextArray(R.array.widget_config_news_count)));
        ArrayAdapter<CharSequence> newsCountAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, newsCountList);
        newsCountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNewsCountSpinner.setAdapter(newsCountAdapter);
        mNewsCountSpinner.setSelection(newsCountAdapter
                .getPosition(String.valueOf(NewsWidgetStorage.loadNewsCount(this, mAppWidgetId))));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_widget_news_configure, menu);
        MenuUtils.whitenMenuItemIcon(this, menu, R.id.action_next);

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

