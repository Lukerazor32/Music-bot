package com.example.telegram_bot.dto;

import lombok.*;

@Data
public class CurrentSongList {
    String fullName;
    String id;
    String track;
    String artist;
    String url;

    @Override
    public String toString() {
        return fullName;
    }
}
