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
import static sejoharp.Notification.notification;
import static sejoharp.Match.emptyMatch;

public class MailSenderTest {
    @Test
    @Ignore
    public void sendsMail() throws MessagingException {
        Mailer mailer = new MailerImpl(TestData.getConfig());
        Match match = emptyMatch()
                .withTableNumber("2")
                .withDiscipline("GD Vorr.")
                .withRound("2")
                .withTeam1("Aron Schneider / Luciano Auria")
                .withTeam2("Sophia Becker / Benjamin Beintner");

        MimeMessage message = mailer.createMessage(notification(match, "ich@du.de", "1"));
        Transport.send(message);
    }

    @Test
    public void createsMail() throws MessagingException {
        Mailer mailer = new MailerImpl(TestData.getConfig());
        Match match = emptyMatch()
                .withTableNumber("2")
                .withDiscipline("GD Vorr.")
                .withRound("2")
                .withTeam1("Aron Schneider / Luciano Auria")
                .withTeam2("Sophia Becker / Benjamin Beintner");

        MimeMessage message = mailer.createMessage(notification(match, "ich@du.de", "1"));
        assertThat(((InternetAddress) message.getRecipients(RecipientType.TO)[0]).getAddress(), is("ich@du.de"));
    }
}
