package sejoharp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.Test;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static sejoharp.Notification.notification;

public class NotifyPlayerJobTest {
    private TelegramSender telegramSender;
    private TournamentRepository repository;

    @Test
    public void finds2NewMatches() throws MessagingException, JsonParseException, JsonMappingException, IOException {
        // given
        PlayerConfig playerConfig = TestData.get2PlayersConfig();
        NotifyPlayerJob notifyPlayerJob = createJob(() -> playerConfig, new HashSet<>(singletonList("tournamentUrl")));

        // when
        notifyPlayerJob.notifyPlayerForNewMatches();

        // then
        assertThat(repository.getNotifications()).containsExactly(
                toNotification(playerConfig.getPlayers().get(1)),
                toNotification(playerConfig.getPlayers().get(0)));
    }

    private Notification toNotification(Player player) {
        return notification(TestData.getMatch(), player.getChatId());
    }

    @Test
    public void doesNotFindOldMatches() throws IOException, MessagingException {
        NotifyPlayerJob notifyPlayerJob = createJob(TestData::get1PlayerConfig, new HashSet<>(singletonList("tournamentUrl")));

        notifyPlayerJob.notifyPlayerForNewMatches();
        notifyPlayerJob.notifyPlayerForNewMatches();

        verify(telegramSender).sendMessage(any(Notification.class));
    }

    // helpers
    private NotifyPlayerJob createJob(PlayerConfigReader playerConfigReader, HashSet<String> tournamentUrls) throws IOException, MessagingException {
        TournamentParser tournamentParser = document -> singletonList(TestData.getMatch());
        telegramSender = mock(TelegramSenderImpl.class);
        PageReader pageReader = url -> loadTournamentData();
        repository = TournamentRepository.repo(tournamentUrls, new HashSet<>());

        return NotifyPlayerJob.newNotifyPlayerJob(playerConfigReader,
                tournamentParser,
                pageReader,
                telegramSender,
                repository);
    }


    // fixtures
    private String getChatIdFromConfig(PlayerConfig playerConfig, int index) {
        return playerConfig.getPlayers().get(index).getChatId();
    }

    private Document loadTournamentData() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("tournament2.html").getFile());
        try {
            return Jsoup.parse(file, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
