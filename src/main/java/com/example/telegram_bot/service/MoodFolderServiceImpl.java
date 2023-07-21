package com.example.telegram_bot.service;

import com.example.telegram_bot.repository.entity.MoodFolder;
import com.example.telegram_bot.repository.MoodFolderRepository;
import com.example.telegram_bot.repository.entity.TelegramSong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MoodFolderServiceImpl implements MoodFolderService {
    private final MoodFolderRepository moodFolderRepository;

    @Autowired
    public MoodFolderServiceImpl(MoodFolderRepository moodFolderRepository) {
        this.moodFolderRepository = moodFolderRepository;
    }

    @Override
    public void save(MoodFolder moodFolder) {
        moodFolderRepository.save(moodFolder);
    }

    @Override
    public void delete(MoodFolder moodFolder) {
        moodFolderRepository.delete(moodFolder);
    }

    @Override
    public Optional<MoodFolder> findById(Long id) {
        return moodFolderRepository.findById(id);
    }

    @Override
    public List<MoodFolder> findAll() {
        return moodFolderRepository.findAll();
    }
}
