package com.example.servplayer.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servplayer.R;
import com.example.servplayer.adapters.SongListAdapter;
import com.example.servplayer.components.MiniPlayerView;
import com.example.servplayer.models.Song;
import com.example.servplayer.services.MediaPlayerService;
import com.example.servplayer.utils.AudioPlayerUtils;
import com.example.servplayer.utils.MyMediaPlayer;
import com.example.servplayer.utils.PermissionHelper;
import com.example.servplayer.utils.SongLoader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SongListAdapter.OnSongClickListener{

    private RecyclerView recyclerView;
    private TextView noMusicAvailable;
    private MiniPlayerView miniPlayerView;
    private SongListAdapter songListAdapter;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Liên kết các View
        recyclerView = findViewById(R.id.recycler_view);
        noMusicAvailable = findViewById(R.id.no_music_available);
        miniPlayerView = findViewById(R.id.mini_player);
        // Nếu không có bài hát, hiển thị thông báo
        if (SongLoader.loadSongs(this).isEmpty()) {
            noMusicAvailable.setVisibility(View.VISIBLE);
        } else {
            noMusicAvailable.setVisibility(View.GONE);
        }
        songListAdapter = new SongListAdapter(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(songListAdapter);
    }
    private final BroadcastReceiver songChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            miniPlayerView.updateMiniPlayer();
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(songChangedReceiver, new IntentFilter("ACTION_SONG_CHANGED"));
    }
    @Override
    public void onSongClick(Song song, int position) {
        // This method will be called when a song is clicked in the RecyclerView
        MyMediaPlayer.currentIndex = position;
        AudioPlayerUtils.playAudio(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Kiểm tra trạng thái phát nhạc và cập nhật MiniPlayer nếu cần thiết
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Giải phóng tài nguyên nếu cần thiết
    }
}
