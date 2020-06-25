package com.example.dd.view.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dd.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountPhotoAdapter2 extends RecyclerView.Adapter<AccountPhotoAdapter2.ViewHolder> {
    Activity activity;
    List<Integer> res;

    public AccountPhotoAdapter2(Activity activity, List<Integer> res) {
        this.activity = activity;
        this.res = res;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_photo_account, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(activity).load(res.get(position)).into(holder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_photo)
        ImageView imgPhoto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
