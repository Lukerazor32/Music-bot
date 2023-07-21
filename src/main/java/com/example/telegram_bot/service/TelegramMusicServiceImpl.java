package com.example.telegram_bot.service;

import com.example.telegram_bot.repository.TelegramSongRepository;
import com.example.telegram_bot.repository.entity.MoodFolder;
import com.example.telegram_bot.repository.entity.TelegramSong;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TelegramMusicServiceImpl implements TelegramMusicService {
    private final TelegramSongRepository telegramSongRepository;
    private final MoodFolderService moodFolderService;

    @Autowired
    public TelegramMusicServiceImpl(TelegramSongRepository telegramSongRepository, MoodFolderService moodFolderService) {
        this.telegramSongRepository = telegramSongRepository;
        this.moodFolderService = moodFolderService;
    }

    @Override
    public void save(TelegramSong telegramSong) {
        telegramSongRepository.save(telegramSong);
    }

    @Override
    public void save(List<MoodFolder> moodFolder, List<TelegramSong> telegramSongs) {
        for (TelegramSong telegramSong : telegramSongs) {
            for (MoodFolder folder : moodFolder) {
                telegramSong.addFolder(folder);
            }
            telegramSongRepository.save(telegramSong);
        }
    }

    @Override
    public void delete(TelegramSong telegramSong) {
        telegramSongRepository.delete(telegramSong);
    }

    @Transactional
    @Override
    public void deleteSongs(List<Long> telegramSongs) {
        telegramSongRepository.deleteSongs(telegramSongs);
    }

    @Override
    public Optional<TelegramSong> findByDownloadId(Long id) {
        return telegramSongRepository.findByDownloadId(id);
    }

    @Override
    public TelegramSong findByTitle(String title) {
        List<TelegramSong> songs = telegramSongRepository.findAll();

        for (TelegramSong song : songs) {
            if (song.getTitle().toLowerCase().contains(title.toLowerCase())) {
                return song;
            }
        }
        return null;
    }

    @Override
    public List<TelegramSong> findAll() {
        return telegramSongRepository.findAll();
    }
}
