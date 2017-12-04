package sejoharp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.io.File;
import java.io.IOException;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class JobTest {
    private Mailer mailer;

    @Test
    public void finds2NewMatches() throws MessagingException, JsonParseException, JsonMappingException, IOException {
        // given
        Job job = createJob(TestData::getTournament2PlayersConfig);

        // when
        job.notifyPlayerForNewMatches();

        // then
        ArgumentCaptor<MimeMessage> argumentCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailer, times(2)).send(argumentCaptor.capture());
        String recipientAddress = getAddress(argumentCaptor,0);
        String recipientAddress1 = getAddress(argumentCaptor, 1);
        assertThat(recipientAddress, is(TestData.getTournament2PlayersConfig().getPlayers().get(0).getEmail()));
        assertThat(recipientAddress1, is(TestData.getTournament2PlayersConfig().getPlayers().get(1).getEmail()));
    }

    private String getAddress(ArgumentCaptor<MimeMessage> argumentCaptor, int index) throws MessagingException {
        return ((InternetAddress) argumentCaptor.getAllValues().get(index)
                .getRecipients(RecipientType.TO)[0]).getAddress();
    }

    @Test
    public void doesNotFindOldMatches() throws IOException, MessagingException {
        Job job = createJob(TestData::getTournament1PlayerConfig);

        job.notifyPlayerForNewMatches();
        job.notifyPlayerForNewMatches();

        verify(mailer).send(any(MimeMessage.class));
    }

    private Job createJob(TournamentConfigReader tournamentConfigReader) throws IOException, MessagingException {
        Document doc = loadTournamentData();
        Parser parser = elements -> singletonList(TestData.getMatch());
        mailer = mock(Mailer.class);
        PageReader pageReader = url -> doc;
        Answer<MimeMessage> messageAnswer = invocation -> new MailerImpl(TestData.getConfig())
                .createMessage(invocation.getArgumentAt(0, Notification.class));
        when(mailer.createMessage(any(Notification.class))).then(messageAnswer);

        return Job.newJob(tournamentConfigReader, parser, mailer, pageReader);
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
