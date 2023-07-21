package com.example.telegram_bot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Container {
    private Long id;
    private String album;

    @Override
    public String toString() {

        return "Album - " + album;
    }
}
