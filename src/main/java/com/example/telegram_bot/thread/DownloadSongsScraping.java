package com.example.telegram_bot.thread;

import com.example.telegram_bot.service.DownloadSongRequest;

import java.util.List;

public class DownloadSongsScraping implements Runnable {
    private DownloadSongRequest songRequest;
    private final List<String> similarSongs;


    public DownloadSongsScraping(DownloadSongRequest songRequest, List<String> similarSongs) {
        this.songRequest = songRequest;
        this.similarSongs = similarSongs;
    }

    @Override
    public void run() {
        while (songRequest.getCount().get() < similarSongs.size()) {
            String title;
            synchronized (songRequest) {
                title = similarSongs.get(songRequest.countIncrease());
            }
//            System.out.println(title + " - " + songRequest.getCount());
            songRequest.addSong(title);
        }
    }
}
