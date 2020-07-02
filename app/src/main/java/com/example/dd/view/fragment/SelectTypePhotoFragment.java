package com.example.dd.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dd.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectTypePhotoFragment extends BottomSheetDialogFragment {
    @BindView(R.id.txt_recent)
    TextView txtRecent;
    @BindView(R.id.txt_galery)
    TextView txtGalery;
    private View view;
    OnTypeSelected onTypeSelected;

    public SelectTypePhotoFragment(OnTypeSelected onTypeSelected) {
        this.onTypeSelected = onTypeSelected;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_select_type, container, false);
            ButterKnife.bind(this, view);
            initView();
        }
        return view;
    }

    private void initView() {

    }

    @OnClick(R.id.txt_recent)
    public void onTxtRecentClicked() {
        onTypeSelected.onTypeClicked(1);
        dismiss();
    }

    @OnClick(R.id.txt_galery)
    public void onTxtGaleryClicked() {
        onTypeSelected.onTypeClicked(2);
        dismiss();
    }

    public interface OnTypeSelected{
        void onTypeClicked(int type);
    }
}
