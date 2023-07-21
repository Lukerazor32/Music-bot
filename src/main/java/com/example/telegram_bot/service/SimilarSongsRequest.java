package com.example.telegram_bot.service;

import com.example.telegram_bot.dto.CurrentSong;
import com.example.telegram_bot.dto.CurrentSongList;
import com.example.telegram_bot.repository.entity.TelegramSong;
import kong.unirest.Unirest;
import lombok.Getter;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class SimilarSongsRequest {
    @Value("${similar.path}")
    private String currentSongLink = "https://songslikex.com";
    @Getter
    private List<String> similarSongs;
    @Getter
    private volatile AtomicInteger count;

    public SimilarSongsRequest() {
        similarSongs = new CopyOnWriteArrayList<>();
        count = new AtomicInteger(similarSongs.size()-1);
    }

    public List<CurrentSongList> getSongs(String songName) {
        CurrentSong currentSongs = Unirest.get(currentSongLink)
                .queryString("song", songName)
                .queryString("_data", "routes/index")
                .asObject(CurrentSong.class)
                .getBody();

        if (currentSongs != null) {
            return currentSongs.getList();
        }

        return null;
    }

    public void setSimilarSongs(CurrentSongList currentSong) {
        try {
            Connection con = Jsoup.connect(currentSongLink + currentSong.getUrl());
            Connection.Response resp = null;
            resp = con.execute();
            Document doc = null;
            if (resp.statusCode() == 200) {
                doc = con.get();
                Element ol = doc.getElementsByClass("trackList table full").first();
                Elements songs = ol.getElementsByTag("li");
                for (Element song : songs) {
                    if (similarSongs.size() < 100) {
                        String songName = song.getElementsByTag("div").get(1).text();
                        String artist = song.getElementsByTag("div").get(2).text();

                        String track = artist + " - " + songName;
                        similarSongs.add(track);
                    }
                    else {
                        return;
                    }
                }
                recursiveCall();
            }
            else {
                recursiveCall();
            }
        } catch (IOException e) {
            System.out.println("\n\n" + e.getMessage() + "\n\n");
            setSimilarSongs(currentSong);
        }
    }

    public void recursiveCall() {
        System.out.println(count.get());
        List<CurrentSongList> similarSong = null;
        while (similarSong == null || similarSong.size() == 0) {
            String name;
            name = similarSongs.get(count.getAndIncrement());

            similarSong = getSongs(name);
        }
        setSimilarSongs(similarSong.get(0));
    }
}
