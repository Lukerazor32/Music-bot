package com.example.telegram_bot.service;

import com.example.telegram_bot.dto.Song;
import com.example.telegram_bot.repository.entity.TelegramSong;
import kong.unirest.GenericType;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import lombok.Getter;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DownloadSongRequest implements RequestService {

    @Value("${search.path}")
    private String searchPath = "https://s2.lillill.li/unknown";

    @Value("${download.path}")
    private String downloadPath = "https://hub.ilill.li";

    @Getter
    private List<TelegramSong> telegramSongs;

    @Getter
    private Map<Long, String> inputMedia;

    @Getter
    private List<Long> nullSongs;

    @Getter
    private volatile AtomicInteger count;

    public DownloadSongRequest() {
        telegramSongs = new ArrayList<>();
        inputMedia = new ConcurrentHashMap<>();
        nullSongs = new ArrayList<>();
        count = new AtomicInteger(0);
    }

    @Override
    public void addSong(String track) {
        List<Song> jsonResponse
                = Unirest.get(searchPath)
                .queryString("q", track)
                .asObject(new GenericType<List<Song>>() {
                })
                .getBody();

        for (Song song : jsonResponse) {
            if (song.getTitle().toLowerCase().contains(track.toLowerCase())) {
                TelegramSong telegramSong = new TelegramSong();
                telegramSong.setArtist(song.getArtist());
                telegramSong.setTitle(song.getTitle());
                telegramSong.setDownloadId(song.getId());
                telegramSong.setDuration(song.getDuration());

                telegramSongs.add(telegramSong);
                return;
            }
        }
    }

    @Override
    public String getLink(Long id) {
        HttpResponse<JsonNode> jsonResponse2;
        JSONObject jsonObject = null;

        long startTime = System.currentTimeMillis();

        boolean check = true;

        while (check) {
            jsonResponse2
                    = Unirest.get(downloadPath)
                    .queryString("id", id)
                    .asJson();

            try {
                jsonObject = jsonResponse2.getBody().getObject();
            } catch (NullPointerException e) {
                return null;
            } catch (JSONException e) {
                return null;
            }
            int passTime = (int) ((System.currentTimeMillis() - startTime) / 1000);
            if (passTime > 5) {
                return null;
            }
            if (jsonObject.has("force")) {
                check = false;
            }
        }
        String link = jsonObject.getString("force");

        URL url = null;
        float file_size = 0;
        try {
            url = new URL(link);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();

            file_size = urlConnection.getContentLength();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (SocketTimeoutException e) {
            System.out.println(e);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


        if (file_size >= 5.0f || file_size == 0) {
            return null;
        }

        if (inputMedia.size() < 5) {
            return link;
        }

        return "fullMedia";
    }

    public int countIncrease() {
        return count.getAndIncrement();
    }
}
