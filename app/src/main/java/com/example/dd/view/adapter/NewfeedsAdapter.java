package com.example.dd.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dd.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewfeedsAdapter extends RecyclerView.Adapter<NewfeedsAdapter.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_newfeed,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtTime;
        TextView txtContent;
        CircleImageView imgAvatar;
        ImageView imgContent;
        CheckBox cbLike;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtContent = itemView.findViewById(R.id.tct_content);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            imgContent = itemView.findViewById(R.id.img_content);
            cbLike = itemView.findViewById(R.id.txt_like);
        }
    }
}
