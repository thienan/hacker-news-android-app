package com.marcelljee.hackernews.screen.news.item.text;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelljee.hackernews.databinding.FragmentDetailsTextBinding;
import com.marcelljee.hackernews.databinding.component.AppDataBindingComponent;
import com.marcelljee.hackernews.fragment.ToolbarFragment;
import com.marcelljee.hackernews.databinding.viewmodel.SectionNewsDetailsViewModel;

public class DetailsTextFragment extends ToolbarFragment {

    private static final String ARG_TEXT = "com.marcelljee.hackernews.screen.news.item.text.arg.TEXT";

    public static DetailsTextFragment newInstance(String text) {
        DetailsTextFragment fragment = new DetailsTextFragment();

        Bundle args = createArguments(text);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        String text = args.getString(ARG_TEXT);

        FragmentDetailsTextBinding binding = FragmentDetailsTextBinding.inflate(inflater, container,
                false, new AppDataBindingComponent(getToolbarActivity()));
        binding.setText(text);

        binding.sectionNewsDetails.setViewModel(new SectionNewsDetailsViewModel(getToolbarActivity()));
        binding.sectionNewsDetails.tvNewsDetails.setMaxLines(Integer.MAX_VALUE);
        binding.sectionNewsDetails.tvNewsDetails.setMovementMethod(LinkMovementMethod.getInstance());

        binding.sectionNewsDetails.flNewsDetails.setBackground(null);

        return binding.getRoot();
    }

    private static Bundle createArguments(String text) {
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);

        return args;
    }
}
