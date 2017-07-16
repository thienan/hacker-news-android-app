package com.marcelje.hackernews.screen.news.details.text;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.databinding.FragmentDetailsTextBinding;

public class DetailsTextActivityFragment extends Fragment {

    private static final String ARG_TEXT = "com.marcelje.hackernews.screen.news.details.arg.TEXT";

    private String mText;

    public static DetailsTextActivityFragment newInstance(String text) {
        DetailsTextActivityFragment fragment = new DetailsTextActivityFragment();

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

        // TODO: find a better way to remove maxLines
        binding.sectionNewsDetails.tvNewsDetails.setMaxLines(Integer.MAX_VALUE);
        binding.sectionNewsDetails.setText(mText);

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
