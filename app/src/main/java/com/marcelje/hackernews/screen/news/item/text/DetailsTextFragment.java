package com.marcelje.hackernews.screen.news.item.text;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.databinding.FragmentDetailsTextBinding;
import com.marcelje.hackernews.fragment.ToolbarFragment;

public class DetailsTextFragment extends ToolbarFragment {

    private static final String ARG_TEXT = "com.marcelje.hackernews.screen.news.item.text.arg.TEXT";

    private String mText;

    public static DetailsTextFragment newInstance(String text) {
        DetailsTextFragment fragment = new DetailsTextFragment();

        Bundle args = createArguments(text);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDetailsTextBinding binding = FragmentDetailsTextBinding.inflate(inflater, container, false);
        binding.setActivity(getToolbarActivity());
        binding.setText(mText);

        // TODO: find a better way to remove maxLines
        binding.sectionNewsDetails.tvNewsDetails.setMaxLines(Integer.MAX_VALUE);
        binding.sectionNewsDetails.tvNewsDetails.setMovementMethod(LinkMovementMethod.getInstance());
        binding.sectionNewsDetails.tvNewsDetails.setBackground(null);

        return binding.getRoot();
    }

    private static Bundle createArguments(String text) {
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);

        return args;
    }

    private void extractArguments() {
        Bundle args = getArguments();

        if (args.containsKey(ARG_TEXT)) {
            mText = args.getString(ARG_TEXT);
        }
    }
}
