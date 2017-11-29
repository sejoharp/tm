package sejoharp;

import java.util.List;

public class TournamentConfig {
    private String url;
    private List<Player> players;

    private TournamentConfig(String url, List<Player> players) {
        this.url = url;
        this.players = players;
    }

    private TournamentConfig() {
    }

    public static TournamentConfig tournamentConfig(String url, List<Player> players) {
        return new TournamentConfig(url, players);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((players == null) ? 0 : players.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
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
        TournamentConfig other = (TournamentConfig) obj;
        if (players == null) {
            if (other.players != null)
                return false;
        } else if (!players.equals(other.players))
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TournamentConfig [url=" + url + ", players=" + players + "]";
    }
}
