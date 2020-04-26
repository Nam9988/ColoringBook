package com.example.dd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {
    List<Integer> colors;
    LayoutInflater inflater;
    OnColorSelected onColorSelected;

    public ColorAdapter(Context context,List<Integer> colors,OnColorSelected onColorSelected) {
        inflater = LayoutInflater.from(context);
        this.colors = colors;
        this.onColorSelected = onColorSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_color, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int color = colors.get(position);
        holder.imgColor.setBackgroundColor(color);
        holder.imgColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onColorSelected.onSelected(color);
            }
        });
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgColor;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgColor = itemView.findViewById(R.id.img_color);
        }
    }

    public interface OnColorSelected{
        void onSelected(int color);
    }
}
