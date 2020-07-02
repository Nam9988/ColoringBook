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
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_new_feed, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView(View view) {
        rvNewFeed = view.findViewById(R.id.rv_layout);
        NewfeedsAdapter adapter = new NewfeedsAdapter(getActivity());
        rvNewFeed.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNewFeed.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        ContainerActivity.getInstance().tbTitle.setText("News Feed");
        ContainerActivity.getInstance().btnChangePhoto.setVisibility(View.GONE);
        super.onResume();
    }
}