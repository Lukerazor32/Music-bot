package com.example.telegram_bot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Song {
    private String title;
    private Long id;
    private Container container;
    private String artist;
    private String duration;
    private Integer bpm;
    private String provider;


    @Override
    public String toString() {

        return title + "\n"
                + container.toString() + ")\nArtist - "
                + artist + " \nDuration - "
                + duration;
    }
}
