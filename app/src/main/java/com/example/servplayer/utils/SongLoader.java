package com.example.servplayer.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.media.MediaMetadataRetriever;

import com.example.servplayer.models.Song;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SongLoader {

    // Biến tĩnh để lưu trữ danh sách bài hát
    public static ArrayList<Song> songsList = null;

    // Phương thức để lấy danh sách bài hát
    public static ArrayList<Song> loadSongs(Context context) {
        // Nếu danh sách bài hát đã được tải trước đó, trả về luôn
        if (songsList != null) {
            return songsList;
        }

        // Nếu chưa, tải danh sách bài hát từ bộ nhớ
        songsList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, projection, selection, null, sortOrder);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String songTitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String songPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                String songDuration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                File songFile = new File(songPath);
                if (songFile.exists()) {
                    // Lấy ảnh nhúng trong MP3
                    byte[] embeddedPicture = getEmbeddedPicture(songPath);

                    Song song = new Song(songPath, songTitle, songDuration, embeddedPicture);
                    songsList.add(song);
                }
            }
            cursor.close();
        }

        return songsList;
    }

    private static byte[] getEmbeddedPicture(String filePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            return retriever.getEmbeddedPicture(); // Có thể là null nếu không có ảnh
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                retriever.release();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Phương thức để làm mới dữ liệu (nếu cần)
    public static void refreshSongsList() {
        songsList = null; // Xóa danh sách cũ để lần sau tải lại
    }
}
