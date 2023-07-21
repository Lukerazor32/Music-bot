package command;

import com.example.telegram_bot.repository.entity.TelegramSong;
import com.example.telegram_bot.service.TelegramMusicService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class ConfirmCommandTest {

    @Sql(scripts = {"/sql/clearDbs.sql", "/sql/telegram_song.sql"})
    @Test
    public void shouldProperlyDeleteAllEqualsSongs() {
        TelegramMusicService telegramMusicService = Mockito.mock(TelegramMusicService.class);
        List<TelegramSong> newSongs = new ArrayList<>();

        TelegramSong telegramSong1 = new TelegramSong();
        telegramSong1.setArtist("song1");
        telegramSong1.setTitle("artist1");
        telegramSong1.setDuration("2");
        telegramSong1.setDownloadId((long) 3);
        newSongs.add(telegramSong1);

        TelegramSong telegramSong2 = new TelegramSong();
        telegramSong2.setArtist("song2");
        telegramSong2.setTitle("artist1");
        telegramSong2.setDuration("2");
        telegramSong2.setDownloadId((long) 1);
        newSongs.add(telegramSong2);

        List<TelegramSong> telegramSongs = telegramMusicService.findAll();

        Iterator<TelegramSong> iterator = newSongs.iterator();

        while (iterator.hasNext()) {
            TelegramSong newSong = iterator.next();
            String title = newSong.getTitle().toLowerCase();
            String artist = newSong.getArtist().toLowerCase();
            for (TelegramSong ts : telegramSongs) {
                if (ts.getTitle().toLowerCase().contains(title) && ts.getArtist().toLowerCase().contains(artist)) {
                    iterator.remove();
                    break;
                }
            }
        }

        Assertions.assertEquals(1, telegramSongs.size());
        Assertions.assertEquals((long) 1, telegramSongs.get(0).getDownloadId());
    }
}
