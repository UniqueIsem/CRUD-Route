package com.example.crud_route;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ItemFileAdapter extends RecyclerView.Adapter<ItemFileAdapter.ImageViewHolder> {
    private Context context;
    private List<String> imgUrls;

    public ItemFileAdapter(Context context, List<String> imgUrls) {
        this.context = context;
        this.imgUrls = imgUrls;
    }

    @NonNull
    @Override
    public ItemFileAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_route_img, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemFileAdapter.ImageViewHolder holder, int position) {
        String imageUrl = imgUrls.get(position);
        Glide.with(context)
                .load(imageUrl)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imgUrls.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
