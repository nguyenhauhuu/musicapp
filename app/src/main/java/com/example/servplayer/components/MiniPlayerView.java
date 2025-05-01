package com.example.servplayer.components;

import static com.example.servplayer.utils.SongLoader.songsList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.servplayer.R;
import com.example.servplayer.activities.MusicPlayerActivity;
import com.example.servplayer.models.Song;
import com.example.servplayer.utils.AudioPlayerUtils;
import com.example.servplayer.utils.MyMediaPlayer;

public class MiniPlayerView extends RelativeLayout {

    private ImageView songImageView;
    private TextView songTitleTextView;
    private ImageButton playPauseButton;
    private MediaPlayer mediaPlayer;

    public MiniPlayerView(Context context) {
        super(context);
        init(context);
    }

    public MiniPlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.mini_player, this);
        songImageView = findViewById(R.id.mini_player_img);
        songTitleTextView = findViewById(R.id.mini_player_title);
        playPauseButton = findViewById(R.id.mini_player_play_pause);
        mediaPlayer = MyMediaPlayer.getInstance();

        // ✅ Xử lý Play/Pause cứng
        playPauseButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                AudioPlayerUtils.pauseAudio(context);
                updatePlayPauseButton(false);
            } else {
                AudioPlayerUtils.resumeAudio(context);
                updatePlayPauseButton(true);
            }
        });

        // ✅ Mở MusicPlayerActivity khi click MiniPlayer
        this.setOnClickListener(v -> {
            Intent intent = new Intent(context, MusicPlayerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Vì context không phải là Activity
            context.startActivity(intent);
        });
    }

    public void setSongTitle(String title) {
        songTitleTextView.setText(title);
    }

    public void setSongImage(Bitmap bitmap) {
        songImageView.setImageBitmap(bitmap);
    }

    public void updatePlayPauseButton(boolean isPlaying) {
        playPauseButton.setImageResource(
                isPlaying ? R.drawable.baseline_pause_45 : R.drawable.baseline_play_arrow_50
        );
    }
    public void updateMiniPlayer() {
        // Hiển thị MiniPlayer
        setVisibility(View.VISIBLE);
        // Cập nhật tiêu đề bài hát
        setSongTitle(songsList.get(MyMediaPlayer.currentIndex).getTitle());

        // Cập nhật hình ảnh bài hát
        byte[] imageBytes = songsList.get(MyMediaPlayer.currentIndex).getEmbeddedPicture();
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            setSongImage(bitmap);
        } else {
            setSongImage(BitmapFactory.decodeResource(getResources(), R.drawable.no_music));
        }
    }
}
