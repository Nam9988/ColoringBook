package com.example.dd.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.dd.R;
import com.example.dd.view.activity.ContainerActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaintFragment extends Fragment {

    public PaintFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paint, container, false);
    }

    @Override
    public void onResume() {
        ContainerActivity.getInstance().tbTitle.setText("Vẽ tranh");
        super.onResume();
    }
}
