package com.example.dd.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dd.R;
import com.example.dd.view.adapter.CommentAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentFragment extends BottomSheetDialogFragment {
    @BindView(R.id.rv_comment)
    RecyclerView rvComment;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_comment, container, false);
            ButterKnife.bind(this, view);
            initRecyclerView();
        }
        return view;
    }

    private void initRecyclerView() {
        rvComment.setAdapter(new CommentAdapter(getActivity()));
    }
}
