package com.marcelje.hackernews.screen.user;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcelje.hackernews.api.HackerNewsApi;
import com.marcelje.hackernews.databinding.FragmentUserBinding;
import com.marcelje.hackernews.factory.SnackbarFactory;
import com.marcelje.hackernews.model.User;

public class UserActivityFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_USER_ID = "com.marcelje.hackernews.screen.user.arg.USER_ID";

    private FragmentUserBinding mBinding;
    private String mUserId;

    public static UserActivityFragment newInstance(String userId) {
        UserActivityFragment fragment = new UserActivityFragment();

        Bundle args = createArguments(userId);
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
        mBinding = FragmentUserBinding.inflate(inflater, container, false);

        if (savedInstanceState == null) {
            retrieveUser();
        }

        return mBinding.getRoot();
    }

    @Override
    public void onClick(View view) {
        retrieveUser();
    }

    private static Bundle createArguments(String userId) {
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);

        return args;
    }

    private void extractArguments() {
        Bundle args = getArguments();

        if (args.containsKey(ARG_USER_ID)) {
            mUserId = args.getString(ARG_USER_ID);
        }
    }

    private void retrieveUser() {
        HackerNewsApi.with(getActivity()).getUser(mUserId, new HackerNewsApi.RestCallback<User>() {
            @Override
            public void onSuccess(User data) {
                mBinding.setUser(data);
            }

            @Override
            public void onFailure(String message) {
                SnackbarFactory
                        .createRetrieveErrorSnackbar(mBinding.getRoot(),
                                UserActivityFragment.this).show();
            }
        });
    }
}
