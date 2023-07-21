package com.example.telegram_bot.thread;

import com.example.telegram_bot.repository.entity.TelegramSong;
import com.example.telegram_bot.service.DownloadSongRequest;
import com.pengrad.telegrambot.model.request.InputMediaAudio;

import java.util.List;

public class FindLinkScraping implements Runnable {
    private final List<TelegramSong> songsList;
    private DownloadSongRequest downloadRequest;

    public FindLinkScraping(List<TelegramSong> songsList, DownloadSongRequest downloadRequest) {
        this.songsList = songsList;
        this.downloadRequest = downloadRequest;
    }

    @Override
    public void run() {
        while (downloadRequest.getInputMedia().size() < 5) {
            TelegramSong ts;
            ts = songsList.get(downloadRequest.countIncrease());

            if (ts.getFileId() == null) {
                String link = downloadRequest.getLink(ts.getDownloadId());

                if (link != null && !link.equals("fullMedia")) {
                    downloadRequest.getInputMedia().put(ts.getDownloadId(), link);
                    System.out.println(downloadRequest.getInputMedia().size());
                    System.out.println(link);
                }
                else if (link == null) {
                    downloadRequest.getNullSongs().add(ts.getDownloadId());
                }
            }
            else {
                downloadRequest.getInputMedia().put(ts.getDownloadId(), ts.getFileId());
            }

        }
    }
}
