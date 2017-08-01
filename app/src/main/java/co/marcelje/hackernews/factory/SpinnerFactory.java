package co.marcelje.hackernews.factory;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import co.marcelje.hackernews.R;

public class SpinnerFactory {

    public static Spinner createSpinner(AppCompatActivity activity,
                                        AdapterView.OnItemSelectedListener listener) {

        Context context;

        if (activity.getSupportActionBar() != null) {
            context = activity.getSupportActionBar().getThemedContext();
        } else {
            context = activity.getApplicationContext();
        }

        Spinner spinner = new Spinner(context);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.settings_type_options, R.layout.item_spinner);

        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);

        return spinner;
    }
}
