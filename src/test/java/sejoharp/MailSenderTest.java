package sejoharp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.Ignore;
import org.junit.Test;

public class MailSenderTest {

	@Test
	public void formatsMail() {
		Mailer mailer = new Mailer();

		Match match = new Match();
		match.setTableNumber("2");
		match.setDisciplin("GD Vorr.");
		match.setRound("2");
		match.setTeam1("Aron Schneider / Luciano Auria");
		match.setTeam2("Sophia Becker / Benjamin Beintner");

		assertThat(
				mailer.formatSubject(match),
				is("Tisch:2 | Disziplin:GD Vorr. | Runde:2 | Aron Schneider / Luciano Auria vs. Sophia Becker / Benjamin Beintner"));
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

		MimeMessage message = mailer.createMessage(new Notification(match, "ich@du.de"));
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

		MimeMessage message = mailer.createMessage(new Notification(match, "ich@du.de"));
		assertThat(
				((InternetAddress) message.getRecipients(RecipientType.TO)[0]).getAddress(),
				is("ich@du.de"));
	}
}
