package sejoharp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Arrays;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RunWith(MockitoJUnitRunner.class)
public class JobMockTest {
	@InjectMocks
	private Job job;
	@Mock
	private Mailer mailer;
	@Mock
	private Downloader downloader;
	@Mock
	private Parser parser;
	
	private Document doc;

	@Before
	public void before() throws IOException, MessagingException, URISyntaxException {
		doc = loadTournamentData();

		when(downloader.getPage(anyString())).thenReturn(doc);
		when(downloader.getTournamentConfig()).thenReturn(TestData.getTournament1PlayerConfig());
		when(parser.getRunningMatchesSnippet(any(Document.class))).thenReturn(loadRunningMatches());
		when(mailer.createMessage(any(Notification.class))).then(new Answer<MimeMessage>() {
			@Override
			public MimeMessage answer(InvocationOnMock invocation) throws Throwable {
				return new Mailer(TestData.getConig()).createMessage(invocation.getArgumentAt(0, Notification.class));
			}
		});
	}

	@Test
	public void finds2NewMatches() throws MessagingException, JsonParseException, JsonMappingException, IOException {
		when(parser.getMatches(any(Elements.class))).thenReturn(Arrays.asList(TestData.getMatch()));
		when(downloader.getTournamentConfig()).thenReturn(TestData.getTournament2PlayersConfig());
		
		job.notifyPlayerForNewMatches();

		ArgumentCaptor<MimeMessage> argumentCaptor = ArgumentCaptor.forClass(MimeMessage.class);
		verify(mailer, times(2)).send(argumentCaptor.capture());
		String recipientAddress = ((InternetAddress) argumentCaptor.getAllValues().get(0).getRecipients(RecipientType.TO)[0])
				.getAddress();
		String recipientAddress1 = ((InternetAddress) argumentCaptor.getAllValues().get(1).getRecipients(RecipientType.TO)[0])
				.getAddress();
		assertThat(recipientAddress, is(TestData.getTournament2PlayersConfig().getPlayers().get(0).getEmail()));
		assertThat(recipientAddress1, is(TestData.getTournament2PlayersConfig().getPlayers().get(1).getEmail()));
	}

	@Test
	public void doesNotFindOldMatches() throws IOException, MessagingException {
		when(parser.getMatches(any(Elements.class))).thenReturn(Arrays.asList(TestData.getMatch()));

		job.notifyPlayerForNewMatches();
		job.notifyPlayerForNewMatches();
		
		verify(mailer).send(any(MimeMessage.class));
	}

	private Document loadTournamentData() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("tournament.html").getFile());
		return Jsoup.parse(file, "ISO-8859-1");
	}

	private Elements loadRunningMatches() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("runningMatchesSnippet.html").getFile());
		return Jsoup.parse(file, "ISO-8859-1").siblingElements();
	}

}
