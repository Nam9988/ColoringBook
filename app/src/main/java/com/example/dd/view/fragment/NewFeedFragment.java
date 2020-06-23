package com.example.dd.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dd.R;
import com.example.dd.view.activity.ContainerActivity;
import com.example.dd.view.adapter.NewfeedsAdapter;

public class NewFeedFragment extends Fragment {
    private RecyclerView rvNewFeed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView(View view) {
        rvNewFeed = view.findViewById(R.id.rv_layout);
        NewfeedsAdapter adapter = new NewfeedsAdapter();
        rvNewFeed.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNewFeed.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        ContainerActivity.getInstance().tbTitle.setText("Bảng tin");
        super.onResume();
    }
}