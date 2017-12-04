package sejoharp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class MailerImpl implements Mailer {
    private final Config config;

    @Autowired
    public MailerImpl(Config config) {
        this.config = config;
    }

    @Override
    public MimeMessage createMessage(Notification notification) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", config.getSmtpserver());
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", config.getPort());
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getUser(), config.getPassword());
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(config.getSenderaddress()));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(notification.getEmail()));
        message.setSubject(notification.getMatch().toFormattedString());
        message.setText(formatBody(notification.getMatch()));
        return message;
    }

    private String formatBody(Match match) {
        return String.format("Tisch:%s\n" + "Disziplin:%s\n" + "Runde:%s\n" + "Teilnehmer:%s vs. %s",
                match.getTableNumber(), match.getDiscipline(), match.getRound(), match.getTeam1(), match.getTeam2());
    }
}
