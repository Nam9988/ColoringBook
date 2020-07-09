package com.example.dd.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dd.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareFragment extends BottomSheetDialogFragment {
    @BindView(R.id.edt_content)
    EditText edtContent;
    @BindView(R.id.btn_share)
    TextView btnShare;
    OnShareClicked onShareClicked;

    public ShareFragment(OnShareClicked onShareClicked) {
        this.onShareClicked = onShareClicked;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

    }

    @OnClick(R.id.btn_share)
    public void onViewClicked() {
        onShareClicked.onShare(edtContent.getText().toString());
        dismissAllowingStateLoss();
    }

    public interface OnShareClicked {
        void onShare(String content);
    }
}
