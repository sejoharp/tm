package sejoharp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Player {
    private final String name;
    private final String chatId;


    @JsonCreator
    private Player(@JsonProperty("name") String name,
                   @JsonProperty("chatId") String chatId) {
        this.name = name;
        this.chatId = chatId;
    }

    public static Player createPlayer(String name, String chatId) {
        return new Player(name, chatId);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", chatId='" + chatId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name) &&
                Objects.equals(chatId, player.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, chatId);
    }

    public String getChatId() {
        return chatId;
    }

}
