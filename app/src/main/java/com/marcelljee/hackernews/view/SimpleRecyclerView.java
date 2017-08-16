package com.marcelljee.hackernews.view;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.marcelljee.hackernews.R;
import com.marcelljee.hackernews.listener.EndlessRecyclerViewScrollListener;

public class SimpleRecyclerView extends FrameLayout {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private View mEmptyView;
    private EndlessRecyclerViewScrollListener mScrollListener;

    public interface OnLoadMoreListener {
        void onLoadMore(int page, int totalItemsCount);
    }

    public SimpleRecyclerView(@NonNull Context context) {
        super(context);
        initView();
    }

    public SimpleRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SimpleRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mRecyclerView.getLayoutManager().onRestoreInstanceState(ss.layoutManagerState);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.layoutManagerState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        return ss;
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.simple_recycler_view, this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mEmptyView = view.findViewById(R.id.empty_view);
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        mRecyclerView.setLayoutManager(manager);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void showDivider() {
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mScrollListener = new EndlessRecyclerViewScrollListener(
                (LinearLayoutManager) mRecyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                listener.onLoadMore(page, totalItemsCount);
            }
        };

        mRecyclerView.addOnScrollListener(mScrollListener);
    }

    public void restartOnLoadMoreListener() {
        mScrollListener.restart();
    }

    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
    }

    public void hideProgressBar() {
        if (mRecyclerView.getAdapter().getItemCount() > 0) {
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(VISIBLE);
        }
    }

    private static class SavedState extends BaseSavedState {
        Parcelable layoutManagerState;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            layoutManagerState = in.readParcelable(RecyclerView.LayoutManager.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeParcelable(layoutManagerState, flags);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
