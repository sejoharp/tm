package sejoharp;

import java.util.Objects;

public class Notification {
    private final Match match;
    private final String chatId;

    private Notification(Match match, String chatId) {
        this.match = match;
        this.chatId = chatId;
    }

    public static Notification notification(Match match, String chatId) {
        return new Notification(match, chatId);

    }

    public Match getMatch() {
        return match;
    }

    public String getChatId() {
        return chatId;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "match=" + match +
                ", chatId='" + chatId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(match, that.match) &&
                Objects.equals(chatId, that.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(match, chatId);
    }

}
