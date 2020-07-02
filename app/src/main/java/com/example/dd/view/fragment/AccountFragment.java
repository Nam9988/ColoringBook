package com.example.dd.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dd.R;
import com.example.dd.view.activity.ContainerActivity;
import com.example.dd.view.adapter.AccountPhotoAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {
    @BindView(R.id.img_avt)
    CircleImageView imgAvt;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.btn_edit)
    Button btnEdit;
    @BindView(R.id.rv_photo)
    RecyclerView rvPhoto;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_account, container, false);
            ButterKnife.bind(this, view);
            initRecyclerView();
        }
        return view;
    }

    private void initRecyclerView() {
        AccountPhotoAdapter accountPhotoAdapter = new AccountPhotoAdapter(getActivity());
        rvPhoto.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rvPhoto.setAdapter(accountPhotoAdapter);
    }

    @Override
    public void onResume() {
        ContainerActivity.getInstance().tbTitle.setText("Tài khoản");
        ContainerActivity.getInstance().btnChangePhoto.setVisibility(View.GONE);
        super.onResume();
    }

    @OnClick(R.id.btn_edit)
    public void onViewClicked() {
    }
}