package com.example.servplayer.activities;

import static com.example.servplayer.utils.AudioPlayerUtils.convertToMMS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.servplayer.R;
import com.example.servplayer.components.MiniPlayerView;
import com.example.servplayer.models.Song;
import com.example.servplayer.services.MediaPlayerService;
import com.example.servplayer.utils.AudioPlayerUtils;
import com.example.servplayer.utils.MyMediaPlayer;
import com.example.servplayer.utils.SongLoader;

public class MusicPlayerActivity extends AppCompatActivity {

    private ImageView artworkView;
    private TextView titleView, currentTime, totalTime;
    private ImageButton playPauseButton, nextButton, prevButton, suffleButton, repeatButton;
    private SeekBar seekBar;
    private Song currentSong;
    private Handler handler = new Handler();
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();

    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private Runnable updateSeekRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                currentTime.setText(convertToMMS(mediaPlayer.getCurrentPosition() + ""));
                handler.postDelayed(this, 500);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        initViews();
        loadSongData();
        setupSeekBar();
        setupControlEvents();
    }

    private void initViews() {
        artworkView = findViewById(R.id.img_song);
        titleView = findViewById(R.id.currentSongTitle);
        playPauseButton = findViewById(R.id.play_pause_btn);
        nextButton = findViewById(R.id.next_btn);
        prevButton = findViewById(R.id.prev_btn);
        seekBar = findViewById(R.id.seekbar);
        currentTime = findViewById(R.id.current_time);
        totalTime = findViewById(R.id.total_time);
        suffleButton = findViewById(R.id.btn_random);
        repeatButton = findViewById(R.id.btn_repeat);
    }

    private void loadSongData() {
        currentSong = SongLoader.songsList.get(MyMediaPlayer.currentIndex);
        if (currentSong != null) {
            titleView.setText(currentSong.getTitle());
            totalTime.setText(convertToMMS(currentSong.getDuration()));
            byte[] imageBytes = currentSong.getEmbeddedPicture();
            if (imageBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                artworkView.setImageBitmap(bitmap);
            } else {
                artworkView.setImageResource(R.drawable.no_music);
            }
            if (mediaPlayer != null) {
                seekBar.setMax(mediaPlayer.getDuration());
            }
        }
    }

    private void setupSeekBar() {
        handler.post(updateSeekRunnable);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }


    private void setupControlEvents() {
        updatePlayPauseIcon();

        playPauseButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                AudioPlayerUtils.pauseAudio(this);
            } else {
                AudioPlayerUtils.resumeAudio(this);
            }
            updatePlayPauseIcon();
        });

        nextButton.setOnClickListener(v -> AudioPlayerUtils.nextSong(this));
        prevButton.setOnClickListener(v -> AudioPlayerUtils.prevSong(this));

        suffleButton.setOnClickListener(v -> {
            AudioPlayerUtils.toggleShuffle(this);
            updateShuffleIcon(); // cập nhật icon
        });

        repeatButton.setOnClickListener(v -> {
            AudioPlayerUtils.toggleRepeat(this);
            updateRepeatIcon(); // cập nhật icon
        });
    }

    private void updateShuffleIcon() {
        isShuffle = !isShuffle;
        suffleButton.setAlpha(isShuffle ? 1f : 0.4f);
    }

    private void updateRepeatIcon() {
        isRepeat = !isRepeat;
        repeatButton.setAlpha(isRepeat ? 1f : 0.4f);
    }


    private final BroadcastReceiver songChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadSongData();
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(songChangedReceiver, new IntentFilter("ACTION_SONG_CHANGED"));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(songChangedReceiver);  // Hủy đăng ký khi không còn cần nhận broadcast
    }

    private void updatePlayPauseIcon() {
        int iconRes = !mediaPlayer.isPlaying() ?
                R.drawable.baseline_pause_45 :
                R.drawable.baseline_play_arrow_50;
        playPauseButton.setImageResource(iconRes);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateSeekRunnable);
    }
}
