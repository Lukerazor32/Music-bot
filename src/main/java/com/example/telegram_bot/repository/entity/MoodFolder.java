package com.example.telegram_bot.repository.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "mood_folder")
public class MoodFolder {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "folder_name")
    private String folderName;

    @ManyToMany(mappedBy = "moodFolders", fetch = FetchType.EAGER)
    private List<TelegramSong> songs;
}
