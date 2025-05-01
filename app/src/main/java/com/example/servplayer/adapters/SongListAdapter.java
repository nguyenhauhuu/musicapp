package com.example.servplayer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servplayer.R;
import com.example.servplayer.models.Song;
import com.example.servplayer.utils.SongLoader;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {

    Context context;
    OnSongClickListener onSongClickListener;

    public interface OnSongClickListener {
        void onSongClick(Song song, int position);
    }

    public SongListAdapter( Context context, OnSongClickListener listener) {
        this.context = context;
        this.onSongClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song songData = SongLoader.songsList.get(position);

        holder.titleTextView.setText(songData.getTitle());
        String duration = formatDuration(songData.getDuration());
        holder.durationTextView.setText(duration);

        // Gán ảnh nếu có
        byte[] imageBytes = songData.getEmbeddedPicture();
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.iconImageView.setImageBitmap(bitmap);
        } else {
            holder.iconImageView.setImageResource(R.drawable.no_music); // Ảnh mặc định nếu không có
        }

        holder.itemView.setOnClickListener(view -> {
            if (onSongClickListener != null) {
                onSongClickListener.onSongClick(songData, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return SongLoader.songsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        TextView titleTextView;
        ImageView iconImageView;
        TextView durationTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleView);
            iconImageView = itemView.findViewById(R.id.artworkView);
            durationTextView = itemView.findViewById(R.id.durationView);
            relativeLayout = itemView.findViewById(R.id.song_panel);
            titleTextView.setSelected(true);
        }
    }

    @SuppressLint("DefaultLocale")
    public String formatDuration(String duration) {
        long millis = Long.parseLong(duration);

        int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(millis);
        int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(millis) % 60;

        if (minutes > 60) {
            int hours = minutes / 60;
            minutes %= 60;
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }

        return String.format("%02d:%02d", minutes, seconds);
    }
}
