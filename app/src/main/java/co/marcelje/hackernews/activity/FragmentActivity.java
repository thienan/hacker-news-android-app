package co.marcelje.hackernews.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import co.marcelje.hackernews.R;

public class FragmentActivity<T extends Fragment> extends ToolbarActivity {

    private static final String TAG_FRAGMENT = "co.marcelje.hackernews.activity.tag.FRAGMENT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    protected void setFragment(T fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment, TAG_FRAGMENT)
                .commit();
    }

    protected T getFragment() {
        return (T) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
    }
}
