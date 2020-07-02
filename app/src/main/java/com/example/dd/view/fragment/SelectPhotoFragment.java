package com.example.dd.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dd.R;
import com.example.dd.view.activity.ContainerActivity;
import com.example.dd.view.adapter.AccountPhotoAdapter;
import com.example.dd.view.adapter.AccountPhotoAdapter2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectPhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectPhotoFragment extends Fragment implements AccountPhotoAdapter2.OnItemSelected {
    @BindView(R.id.rv_photo)
    RecyclerView rvPhoto;
    private OnPhotoSelected onPhotoSelected;
    private List<Integer> res = new ArrayList<>();

    private View view;

    public SelectPhotoFragment(OnPhotoSelected onPhotoSelected) {
        // Required empty public constructor
        this.onPhotoSelected = onPhotoSelected;
    }

    public static SelectPhotoFragment newInstance(OnPhotoSelected onPhotoSelected) {
        SelectPhotoFragment fragment = new SelectPhotoFragment(onPhotoSelected);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_select_photo, container, false);
            ButterKnife.bind(this, view);
            initRecyclerView();
        }
        return view;
    }

    private void initRecyclerView() {
        getData();
        AccountPhotoAdapter2 accountPhotoAdapter = new AccountPhotoAdapter2(getActivity(),res,this);
        rvPhoto.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvPhoto.setAdapter(accountPhotoAdapter);
    }

    private void getData() {
        res.add(R.drawable.img_demo);
        res.add(R.drawable.img_demo);
        res.add(R.drawable.img_demo);
        res.add(R.drawable.img_demo);
        res.add(R.drawable.img_demo);
        res.add(R.drawable.img_demo);
        res.add(R.drawable.img_demo);
        res.add(R.drawable.img_demo);
    }

    @Override
    public void onSelected(int res) {
        onPhotoSelected.onSelected(res);
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    public interface OnPhotoSelected {
        void onSelected(int res);
    }

    @Override
    public void onResume() {
        ContainerActivity.getInstance().tbTitle.setText("Chọn ảnh");
        ContainerActivity.getInstance().btnChangePhoto.setVisibility(View.GONE);
        super.onResume();
    }
}