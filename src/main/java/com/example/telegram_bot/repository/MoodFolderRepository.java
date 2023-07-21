package com.example.telegram_bot.repository;

import com.example.telegram_bot.repository.entity.MoodFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoodFolderRepository extends JpaRepository<MoodFolder, Long> {

    Optional<MoodFolder> findById(Long id);

    List<MoodFolder> findAll();
}
