package sejoharp;

import org.junit.Ignore;
import org.junit.Test;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static sejoharp.EmailNotification.notification;
import static sejoharp.Match.emptyMatch;

public class MailSenderTest {

    @Test
    public void formatsMail() {
        Mailer mailer = new Mailer(null);

        Match match = emptyMatch()
                .withTableNumber("2")
                .withDiscipline("GD Vorr.")
                .withRound("2")
                .withTeam1("Aron Schneider / Luciano Auria")
                .withTeam2("Sophia Becker / Benjamin Beintner");

        assertThat(mailer.formatSubject(match), is(
                "Tisch:2 | Disziplin:GD Vorr. | Runde:2 | Aron Schneider / Luciano Auria vs. Sophia Becker / Benjamin Beintner"));
    }

    @Test
    @Ignore
    public void sendsMail() throws MessagingException {
        Mailer mailer = new Mailer(TestData.getConfig());
        Match match = emptyMatch()
                .withTableNumber("2")
                .withDiscipline("GD Vorr.")
                .withRound("2")
                .withTeam1("Aron Schneider / Luciano Auria")
                .withTeam2("Sophia Becker / Benjamin Beintner");

        MimeMessage message = mailer.createMessage(notification(match, "ich@du.de"));
        Transport.send(message);
    }

    @Test
    public void createsMail() throws MessagingException {
        Mailer mailer = new Mailer(TestData.getConfig());
        Match match = emptyMatch()
                .withTableNumber("2")
                .withDiscipline("GD Vorr.")
                .withRound("2")
                .withTeam1("Aron Schneider / Luciano Auria")
                .withTeam2("Sophia Becker / Benjamin Beintner");

        MimeMessage message = mailer.createMessage(notification(match, "ich@du.de"));
        assertThat(((InternetAddress) message.getRecipients(RecipientType.TO)[0]).getAddress(), is("ich@du.de"));
    }
}
