package sejoharp;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

public interface Mailer {
    default void send(MimeMessage message) throws MessagingException {
        Transport.send(message);
    }

    MimeMessage createMessage(Notification notification) throws MessagingException;
}
