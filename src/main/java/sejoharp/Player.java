package sejoharp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Player {
    private final String name;
    private final String email;
    private final String chatId;


    @JsonCreator
    public Player(@JsonProperty("name") String name,
                  @JsonProperty("email") String email,
                  @JsonProperty("chatId") String chatId) {
        this.name = name;
        this.email = email;
        this.chatId = chatId;
    }

    public static Player createPlayer(String name, String email, String chatId) {
        return new Player(name, email, chatId);
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getChatId() {
        return chatId;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", chatId='" + chatId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name) &&
                Objects.equals(email, player.email) &&
                Objects.equals(chatId, player.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, chatId);
    }
}
