package sejoharp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class JobTest {
    private TelegramSender telegramSender;

    @Test
    public void finds2NewMatches() throws MessagingException, JsonParseException, JsonMappingException, IOException {
        // given
        Job job = createJob(TestData::getTournament2PlayersConfig);

        // when
        job.notifyPlayerForNewMatches();

        // then
        ArgumentCaptor<Notification> argumentCaptor = ArgumentCaptor.forClass(Notification.class);
        verify(telegramSender, times(2)).sendMessage(argumentCaptor.capture());
        String recipientAddress = argumentCaptor.getAllValues().get(0).getChatId();
        String recipientAddress1 = argumentCaptor.getAllValues().get(1).getChatId();
        assertThat(recipientAddress, is(TestData.getTournament2PlayersConfig().getPlayers().get(0).getChatId()));
        assertThat(recipientAddress1, is(TestData.getTournament2PlayersConfig().getPlayers().get(1).getChatId()));
    }

    @Test
    public void doesNotFindOldMatches() throws IOException, MessagingException {
        Job job = createJob(TestData::getTournament1PlayerConfig);

        job.notifyPlayerForNewMatches();
        job.notifyPlayerForNewMatches();

        verify(telegramSender).sendMessage(any(Notification.class));
    }

    private Job createJob(TournamentConfigReader tournamentConfigReader) throws IOException, MessagingException {
        Document doc = loadTournamentData();
        Parser parser = elements -> singletonList(TestData.getMatch());
        telegramSender = mock(TelegramSender.class);
        PageReader pageReader = url -> doc;
        return Job.newJob(tournamentConfigReader, parser, pageReader, telegramSender);
    }

    // fixtures

    private Document loadTournamentData() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("tournament2.html").getFile());
        return Jsoup.parse(file, "utf-8");
    }

    private Elements loadRunningMatches() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("runningMatchesSnippet.html").getFile());
        return Jsoup.parse(file, "utf-8").siblingElements();
    }

}
