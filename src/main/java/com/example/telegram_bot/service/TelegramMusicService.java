package com.example.telegram_bot.service;

import com.example.telegram_bot.repository.entity.MoodFolder;
import com.example.telegram_bot.repository.entity.TelegramSong;

import java.util.List;
import java.util.Optional;

public interface TelegramMusicService {

    void save(TelegramSong telegramSong);

    void save(List<MoodFolder> moodFolder, List<TelegramSong> telegramSongs);

    void delete(TelegramSong telegramSong);

    void deleteSongs(List<Long> telegramSongs);

    Optional<TelegramSong> findByDownloadId(Long downloadId);

    TelegramSong findByTitle(String title);

    List<TelegramSong> findAll();
}
