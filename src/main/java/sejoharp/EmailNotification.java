package sejoharp;

public class EmailNotification {
    private final Match match;
    private final String notificationEmail;

    private EmailNotification(Match match, String notificationEmail) {
        this.match = match;
        this.notificationEmail = notificationEmail;
    }

    public static EmailNotification notification(Match match, String notificationEmail) {
        return new EmailNotification(match, notificationEmail);

    }

    public Match getMatch() {
        return match;
    }

    public String getNotificationEmail() {
        return notificationEmail;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((match == null) ? 0 : match.hashCode());
        result = prime * result + ((notificationEmail == null) ? 0 : notificationEmail.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EmailNotification other = (EmailNotification) obj;
        if (match == null) {
            if (other.match != null)
                return false;
        } else if (!match.equals(other.match))
            return false;
        if (notificationEmail == null) {
            if (other.notificationEmail != null)
                return false;
        } else if (!notificationEmail.equals(other.notificationEmail))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "EmailNotification [match=" + match + ", notificationEmail=" + notificationEmail + "]";
    }
}
