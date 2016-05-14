package sejoharp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@RunWith(MockitoJUnitRunner.class)
public class MailSenderTest {

	@Mock
	private JavaMailSender sender;

	@Spy
	private Config config = TestData.getConig();

	@InjectMocks
	private Mailer mailer;

	@Test
	public void formatsAMail() throws MessagingException, IOException {
		Match matchWithMail = TestData.getMatchWithMail();
		Message message = mailer.createMessage(matchWithMail);
		assertThat(message.getSubject(),
				is("Tisch:2 | Disziplin:GD Vorr. | Runde:2 | User1 / User2 vs. User3 / User4"));
		assertThat((String) message.getContent(),
				is("Tisch:2\nDisziplin:GD Vorr.\nRunde:2\nTeilnehmer:User1 / User2 vs. User3 / User4"));
		assertThat(((InternetAddress) message.getAllRecipients()[0]).getAddress(),
				is(matchWithMail.getNotificationEmail()));
	}

	@Test
	public void sendsFormattedMail() throws MessagingException, IOException {
		Match matchWithMail = TestData.getMatchWithMail();
		ArgumentCaptor<SimpleMailMessage> argumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

		mailer.send(matchWithMail);

		verify(sender).send(argumentCaptor.capture());
		assertThat(argumentCaptor.getValue().getSubject(),
				is("Tisch:2 | Disziplin:GD Vorr. | Runde:2 | User1 / User2 vs. User3 / User4"));
		assertThat(argumentCaptor.getValue().getText(),
				is("Tisch:2\nDisziplin:GD Vorr.\nRunde:2\nTeilnehmer:User1 / User2 vs. User3 / User4"));
		assertThat(argumentCaptor.getValue().getTo()[0], is(matchWithMail.getNotificationEmail()));
	}

	@Test
	public void formatsMail() {
		Match match = new Match();
		match.setTableNumber("2");
		match.setDisciplin("GD Vorr.");
		match.setRound("2");
		match.setTeam1("Aron Schneider / Luciano Auria");
		match.setTeam2("Sophia Becker / Benjamin Beintner");

		assertThat(mailer.formatSubject(match), is(
				"Tisch:2 | Disziplin:GD Vorr. | Runde:2 | Aron Schneider / Luciano Auria vs. Sophia Becker / Benjamin Beintner"));
	}

	@Test
	@Ignore
	public void sendsMail() throws MessagingException {
		Mailer mailer = new Mailer();
		Match match = new Match();
		match.setTableNumber("2");
		match.setDisciplin("GD Vorr.");
		match.setRound("2");
		match.setTeam1("Aron Schneider / Luciano Auria");
		match.setTeam2("Sophia Becker / Benjamin Beintner");

		Message message = mailer.createMessage(match);
		Transport.send(message);
	}

	@Test
	public void createsMail() throws MessagingException {
		Config config = TestData.getConig();
		Mailer mailer = new Mailer(config);
		Match match = new Match();
		match.setTableNumber("2");
		match.setDisciplin("GD Vorr.");
		match.setRound("2");
		match.setTeam1("Aron Schneider / Luciano Auria");
		match.setTeam2("Sophia Becker / Benjamin Beintner");
		match.setNotificationEmail("ich@du.de");

		Message message = mailer.createMessage(match);
		assertThat(((InternetAddress) message.getRecipients(RecipientType.TO)[0]).getAddress(), is("ich@du.de"));
	}
}
