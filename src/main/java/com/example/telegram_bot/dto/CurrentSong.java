package com.example.telegram_bot.dto;

import lombok.*;

import java.util.List;

@Data
public class CurrentSong {
    String searchTerm;
    List<CurrentSongList> list;
    String origin;
}
