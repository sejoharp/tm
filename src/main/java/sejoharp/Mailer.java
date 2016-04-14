package sejoharp;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mailer {

	@Autowired
	Config config;

	public Mailer(Config config) {
		this.config = config;
	}

	public Mailer() {
	};

	public MimeMessage createMessage(Match match) throws MessagingException {
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
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(match.getNotificationEmail()));
		message.setSubject(formatSubject(match));
		message.setText(formatBody(match));
		return message;
	}

	public void send(MimeMessage message) throws MessagingException {
		Transport.send(message);
	}

	public String formatSubject(Match match) {
		return String.format("Tisch:%s | Disziplin:%s | Runde:%s | %s vs. %s", match.getTableNumber(),
				match.getDisciplin(), match.getRound(), match.getTeam1(), match.getTeam2());
	}

	public String formatBody(Match match) {
		return String.format("Tisch:%s\n" + "Disziplin:%s\n" + "Runde:%s\n" + "Teilnehmer:%s vs. %s",
				match.getTableNumber(), match.getDisciplin(), match.getRound(), match.getTeam1(), match.getTeam2());
	}
}
