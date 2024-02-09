package com.example.crud_route;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    private ArrayList<String> fileUris;

    public FileAdapter(ArrayList<String> fileUris) {
        this.fileUris = fileUris;
    }

    @NonNull
    @Override
    public FileAdapter.FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileAdapter.FileViewHolder holder, int position) {
        String uriString = fileUris.get(position);
        Uri uri = Uri.parse(uriString);
        Glide.with(holder.itemView.getContext())
                .load(uri)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return fileUris.size();
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
