package com.example.telegram_bot.service;

import com.example.telegram_bot.dto.Song;

import java.util.List;

public interface RequestService {

    void addSong(String singer);

    String getLink(Long id);
}
