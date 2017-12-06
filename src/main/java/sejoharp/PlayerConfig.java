package sejoharp;

import java.util.List;
import java.util.Objects;

public class PlayerConfig {
    private List<Player> players;

    private PlayerConfig(List<Player> players) {
        this.players = players;
    }

    private PlayerConfig() {
    }

    public static PlayerConfig playerConfig(List<Player> players) {
        return new PlayerConfig(players);
    }

    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return "PlayerConfig{" +
                "players=" + players +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerConfig that = (PlayerConfig) o;
        return Objects.equals(players, that.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(players);
    }

}
