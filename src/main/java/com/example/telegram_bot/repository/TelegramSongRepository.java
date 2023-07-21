package com.example.telegram_bot.repository;


import com.example.telegram_bot.repository.entity.TelegramSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TelegramSongRepository extends JpaRepository<TelegramSong, Long> {
    Optional<TelegramSong> findByDownloadId(Long downloadId);

    Optional<TelegramSong> findByArtist(String artist);

    List<TelegramSong> findAll();

    @Modifying
    @Query("delete from TelegramSong u where u.downloadId in ?1")
    void deleteSongs(List<Long> ids);
}
