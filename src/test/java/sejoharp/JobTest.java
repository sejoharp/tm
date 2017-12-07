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

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class JobTest {
    private TelegramSender telegramSender;

    @Test
    public void finds2NewMatches() throws MessagingException, JsonParseException, JsonMappingException, IOException {
        // given
        PlayerConfig playerConfig = TestData.get2PlayersConfig();
        Job job = createJob(() -> playerConfig, new HashSet<>(singletonList("tournamentUrl")));

        // when
        job.notifyPlayerForNewMatches();

        // then
        ArgumentCaptor<Notification> argumentCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(telegramSender, times(2)).sendMessage(argumentCaptor.capture());
        String recipientAddress = argumentCaptor.getAllValues().get(0).getChatId();
        String recipientAddress1 = argumentCaptor.getAllValues().get(1).getChatId();
        assertThat(recipientAddress).isEqualTo(getChatIdFromConfig(playerConfig, 0));
        assertThat(recipientAddress1).isEqualTo(getChatIdFromConfig(playerConfig, 1));
    }

    @Test
    public void doesNotFindOldMatches() throws IOException, MessagingException {
        Job job = createJob(TestData::get1PlayerConfig, new HashSet<>(singletonList("tournamentUrl")));

        job.notifyPlayerForNewMatches();
        job.notifyPlayerForNewMatches();

        verify(telegramSender).sendMessage(any(Notification.class));
    }

    @Test
    public void refreshesInterestingTournaments() throws IOException, MessagingException {
        // given
        TournamentParser tournamentParser = document -> singletonList(TestData.getMatch());
        PageReader pageReader = url -> loadTournamentData();
        telegramSender = mock(TelegramSenderImpl.class);
        TournamentFinder tournamentFinder = (config, knownTournaments) -> new HashSet<>(singletonList("tournamentUrl"));
        PlayerConfig playerConfig = TestData.get1PlayerConfig();
        Job job = Job.newJob(
                () -> playerConfig,
                tournamentParser,
                pageReader,
                telegramSender,
                tournamentFinder,
                new HashSet<>());

        // when
        job.refreshInterestingTournaments();
        job.notifyPlayerForNewMatches();

        // then
        ArgumentCaptor<Notification> argumentCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(telegramSender).sendMessage(argumentCaptor.capture());
        String recipientAddress = argumentCaptor.getValue().getChatId();
        assertThat(recipientAddress).isEqualTo(getChatIdFromConfig(playerConfig, 0));
    }

    // helpers
    private Job createJob(PlayerConfigReader playerConfigReader, HashSet<String> tournamentUrls) throws IOException, MessagingException {
        TournamentParser tournamentParser = document -> singletonList(TestData.getMatch());
        telegramSender = mock(TelegramSenderImpl.class);
        PageReader pageReader = url -> loadTournamentData();
        return Job.newJob(playerConfigReader,
                tournamentParser,
                pageReader,
                telegramSender,
                null,
                tournamentUrls
        );
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
