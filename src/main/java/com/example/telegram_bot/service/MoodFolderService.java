package com.example.telegram_bot.service;

import com.example.telegram_bot.repository.entity.MoodFolder;

import java.util.List;
import java.util.Optional;

public interface MoodFolderService {

    void save(MoodFolder telegramMusic);

    void delete(MoodFolder moodFolder);

    Optional<MoodFolder> findById(Long id);

    List<MoodFolder> findAll();
}
