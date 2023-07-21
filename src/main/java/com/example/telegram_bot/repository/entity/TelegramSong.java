package com.example.telegram_bot.repository.entity;

import lombok.Data;
import org.checkerframework.checker.units.qual.A;

import javax.persistence.*;
import java.util.*;

import static java.util.Objects.isNull;

@Data
@Entity
@Table(name = "tg_song")
public class TelegramSong {
    @Column
    private String title;

    @Id
    @Column(name = "download_id")
    private Long downloadId;

    @Column(name = "file_id")
    private String fileId;

    @Column
    private String artist;

    @Column
    private String duration;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "relationship_song_folder",
            joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "mood_folder_id")
    )
    private List<MoodFolder> moodFolders;

    public void addFolder(MoodFolder moodFolder) {
        if (isNull(moodFolders)) {
            moodFolders = new ArrayList<>();
        }
        moodFolders.add(moodFolder);
        moodFolder.getSongs().add(this);
    }

    public void removeFolder(MoodFolder moodFolder) {
        moodFolders.remove(moodFolder);
    }

}
