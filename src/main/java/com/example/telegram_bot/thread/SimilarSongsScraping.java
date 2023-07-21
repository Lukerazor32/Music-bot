package com.example.telegram_bot.thread;

import com.example.telegram_bot.dto.CurrentSongList;
import com.example.telegram_bot.service.SimilarSongsRequest;

import java.util.List;

public class SimilarSongsScraping implements Runnable {
    private SimilarSongsRequest similarSongsRequest;
    private CurrentSongList currentSong;

    public SimilarSongsScraping(SimilarSongsRequest similarSongsRequest) {
        this.similarSongsRequest = similarSongsRequest;
    }

    public SimilarSongsScraping(SimilarSongsRequest similarSongsRequest, CurrentSongList currentSong) {
        this.similarSongsRequest = similarSongsRequest;
        this.currentSong = currentSong;
    }

    @Override
    public void run() {
        if (similarSongsRequest.getSimilarSongs().size() == 0) {
            similarSongsRequest.setSimilarSongs(currentSong);
        }
        else {
            similarSongsRequest.recursiveCall();
        }

    }
}
