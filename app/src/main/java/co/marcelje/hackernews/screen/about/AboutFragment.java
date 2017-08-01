package co.marcelje.hackernews.screen.about;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.marcelje.hackernews.R;
import co.marcelje.hackernews.databinding.FragmentAboutBinding;
import co.marcelje.hackernews.fragment.ToolbarFragment;
import co.marcelje.hackernews.utils.ItemUtils;

public class AboutFragment extends ToolbarFragment {

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAboutBinding binding = FragmentAboutBinding.inflate(inflater, container, false);
        binding.tvAbout.setText(ItemUtils.fromHtml(getToolbarActivity(), getString(R.string.about_text)));
        binding.tvAbout.setMovementMethod(LinkMovementMethod.getInstance());

        return binding.getRoot();
    }
}
