package com.example.servplayer.utils;

import static com.example.servplayer.utils.SongLoader.songsList;

import android.content.Context;
import android.content.Intent;

import com.example.servplayer.models.Song;
import com.example.servplayer.services.MediaPlayerService;

import java.util.concurrent.TimeUnit;

public class AudioPlayerUtils {
    Song song;
    public static void playAudio(Context context) {
        Intent playIntent = new Intent(context, MediaPlayerService.class);
        playIntent.setAction("service_play_song");
        playIntent.putExtra("media", songsList.get(MyMediaPlayer.currentIndex));
        context.startService(playIntent);
    }

    public static void resumeAudio(Context context) {
        Intent resumeIntent = new Intent(context, MediaPlayerService.class);
        resumeIntent.setAction("service_resume_song");
        context.startService(resumeIntent);
    }

    public static void pauseAudio(Context context) {
        Intent pauseIntent = new Intent(context, MediaPlayerService.class);
        pauseIntent.setAction("service_pause_song");
        context.startService(pauseIntent);
    }

    public static void nextSong(Context context) {
        Intent nextIntent = new Intent(context, MediaPlayerService.class);
        nextIntent.setAction("service_next_song");
        if (MyMediaPlayer.currentIndex < songsList.size() - 1) MyMediaPlayer.currentIndex++;
        else MyMediaPlayer.currentIndex = 0;
        nextIntent.putExtra("media", songsList.get(MyMediaPlayer.currentIndex));
        context.startService(nextIntent);
    }

    public static void prevSong(Context context) {
        Intent prevIntent = new Intent(context, MediaPlayerService.class);
        prevIntent.setAction("service_prev_song");
        if (MyMediaPlayer.currentIndex > 0) MyMediaPlayer.currentIndex--;
        else MyMediaPlayer.currentIndex = songsList.size()-1;
        prevIntent.putExtra("media", songsList.get(MyMediaPlayer.currentIndex));
        context.startService(prevIntent);
    }

    public static void updateSeekBar(Context context, int progress, Song song) {
        Intent seekBarIntent = new Intent(context, MediaPlayerService.class);
        seekBarIntent.setAction("service_seekbar_song");
        seekBarIntent.putExtra("current position", progress);
        seekBarIntent.putExtra("current song", song);
        context.startService(seekBarIntent);
    }

    public static void toggleShuffle(Context context) {
        Intent intent = new Intent(context, MediaPlayerService.class);
        intent.setAction("service_toggle_shuffle");
        context.startService(intent);
    }

    public static void toggleRepeat(Context context) {
        Intent intent = new Intent(context, MediaPlayerService.class);
        intent.setAction("service_toggle_repeat");
        context.startService(intent);
    }


    public static String convertToMMS(String duration) {
        long millis = Long.parseLong(duration);
        int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(millis);
        int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
