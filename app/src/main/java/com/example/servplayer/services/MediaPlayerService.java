package com.example.servplayer.services;

import static com.example.servplayer.utils.SongLoader.songsList;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.example.servplayer.models.Song;
import com.example.servplayer.utils.MyMediaPlayer;

import java.util.Random;

public class MediaPlayerService extends Service {

    private final MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();

    private Song currentSong;
    private boolean isShuffle = false;
    private boolean isRepeat = false;


    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer.setOnCompletionListener(mp -> {
            if (isRepeat) {
                playSong(currentSong); // Lặp lại bài hiện tại
            } else {
                Song nextSong = getNextSong(); // Bài kế tiếp
                if (nextSong != null) {
                    playSong(nextSong);
                } else {
                    stopSelf(); // Hết danh sách thì dừng
                }
            }
        });
    }
    private Song getNextSong() {
        int currentIndex = MyMediaPlayer.currentIndex;

        if (isShuffle) {
            Random random = new Random();
            int randomIndex;
            do {
                randomIndex = random.nextInt(songsList.size());
            } while (randomIndex == currentIndex && songsList.size() > 1); // Không lặp lại cùng bài
            MyMediaPlayer.currentIndex = randomIndex;
        } else {
            currentIndex++;
            if (currentIndex >= songsList.size()) {
                return null; // Hết danh sách
            }
            MyMediaPlayer.currentIndex = currentIndex;
        }

        return songsList.get(MyMediaPlayer.currentIndex);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();

            if (action != null) {
                switch (action) {
                    case "service_play_song":
                        Song song = (Song) intent.getSerializableExtra("media");
                        if (song != null) {
                            playSong(song);
                        }
                        break;

                    case "service_resume_song":
                        resumeSong();
                        break;

                    case "service_pause_song":
                        pauseSong();
                        break;

                    case "service_next_song":
                        Song nextSong = (Song) intent.getSerializableExtra("media");
                        if (nextSong != null) {
                            playSong(nextSong);
                        }
                        break;

                    case "service_prev_song":
                        Song prevSong = (Song) intent.getSerializableExtra("media");
                        if (prevSong != null) {
                            playSong(prevSong);
                        }
                        break;

                    case "service_seekbar_song":
                        int currentPosition = intent.getIntExtra("current position", 0);
                        seekTo(currentPosition);
                        break;

                    case "service_toggle_shuffle":
                        isShuffle = !isShuffle;
                        Log.d("MediaPlayerService", "Shuffle: " + isShuffle);
                        break;

                    case "service_toggle_repeat":
                        isRepeat = !isRepeat;
                        Log.d("MediaPlayerService", "Repeat: " + isRepeat);
                        break;

                }
            }
        }
        return START_NOT_STICKY;
    }

    private void playSong(Song song) {
        try {
            mediaPlayer.reset();
            currentSong = song;
            mediaPlayer.setDataSource(song.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            Log.d("MediaPlayerService", "Playing song: " + song.getTitle());
            Intent intent = new Intent("ACTION_SONG_CHANGED");
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pauseSong() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Log.d("MediaPlayerService", "Song paused");
        }
    }

    private void resumeSong() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            Log.d("MediaPlayerService", "Song resumed");
        }
    }

    private void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
