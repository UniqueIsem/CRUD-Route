package com.example.crud_route;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    Context context;
    private ArrayList<String> fileUris;

    public FileAdapter(Context context, ArrayList<String> fileUris) {
        this.context = context;
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
        String mimetype = context.getContentResolver().getType(Uri.parse(uriString));

        if (mimetype != null && mimetype.startsWith("image")) {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.videoView.setVisibility(View.GONE);
            Glide.with(holder.itemView.getContext())
                    .load(uriString)
                    .into(holder.imageView);
        } else if (mimetype != null && mimetype.startsWith("video")) {
            holder.imageView.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.VISIBLE);
            holder.videoView.setVideoURI(Uri.parse(uriString));
            holder.videoView.pause();
            holder.videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.videoView.isPlaying()) {
                        holder.videoView.pause();
                    } else {
                        holder.videoView.start();
                    }
                }
            });
            holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return fileUris.size();
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        VideoView videoView;
        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            videoView = itemView.findViewById(R.id.videoView);
        }
    }
}
