package com.example.dd.view.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dd.R;
import com.example.dd.view.fragment.CommentFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewfeedsAdapter extends RecyclerView.Adapter<NewfeedsAdapter.ViewHolder> {
    FragmentActivity activity;

    public NewfeedsAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_newfeed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 1){
            holder.txtName.setText("Nam");
            holder.imgAvatar.setImageResource(R.drawable.messi);
            holder.imgContent.setImageResource(R.drawable.demo2);
        }else {
            holder.imgContent.setImageResource(R.drawable.demo1);
        }
        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentFragment fragment = new CommentFragment();
                fragment.show(activity.getSupportFragmentManager(), "CommentFragment");
            }
        });
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_avatar)
        CircleImageView imgAvatar;
        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.tct_content)
        TextView tctContent;
        @BindView(R.id.img_content)
        ImageView imgContent;
        @BindView(R.id.txt_like)
        CheckBox txtLike;
        @BindView(R.id.btn_comment)
        LinearLayout btnComment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
