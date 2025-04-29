package com.example.servplayer;

import android.annotation.SuppressLint;
import android.content.Context;
//import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {

    ArrayList<Song> songsList;
    Context context;
    OnSongClickListener onSongClickListener;

    public interface OnSongClickListener {
        void onSongClick(Song song,int position);
    }

    public SongListAdapter(ArrayList<Song> songsList, Context context, OnSongClickListener listener) {
        this.songsList = songsList;
        this.context = context;
        this.onSongClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Populate the recycler view with the recycler_item.xml file layout
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song songData = songsList.get(position);
        holder.titleTextView.setText(songData.getTitle());
        String duration = formatDuration(songData.getDuration());
        holder.durationTextView.setText(duration);

        holder.itemView.setOnClickListener(view -> {
            if (onSongClickListener != null) {
                onSongClickListener.onSongClick(songData, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    // Create custom class ViewHolder
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

    // Format the milliseconds to seconds
    @SuppressLint("DefaultLocale")
    public String formatDuration (String duration) {
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
