package sejoharp;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

public interface Mailer {
    default void send(MimeMessage message) throws MessagingException {
        Transport.send(message);
    }

    static String formatSubject(Match match) {
        return String.format("Tisch:%s | Disziplin:%s | Runde:%s | %s vs. %s", match.getTableNumber(),
                match.getDiscipline(), match.getRound(), match.getTeam1(), match.getTeam2());
    }

    MimeMessage createMessage(Notification notification) throws MessagingException;
}
