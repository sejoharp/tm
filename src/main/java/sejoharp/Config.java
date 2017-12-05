package sejoharp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Component
public class Config {
    @Value("${tournamentConfigPath}")
    private String tournamentConfigPath;
    @Value("${telegramUrl}")
    private String telegramUrl;

    private Config(String tournamentConfigPath, String telegramUrl) {
        this.tournamentConfigPath = tournamentConfigPath;
        this.telegramUrl = telegramUrl;
    }

    private Config() {
    }

    public static Config emptyConfig() {
        return new Config();
    }

    public String getTournamentConfigPath() {
        return tournamentConfigPath;
    }

    public String getTelegramUrl() {
        return telegramUrl;
    }

    public Config withTournamentConfigPath(String tournamentConfigPath) {
        return new Config(
                tournamentConfigPath,
                telegramUrl);
    }

    public Config withTelegramUrl(String telegramToken) {
        return new Config(
                tournamentConfigPath,
                telegramToken);
    }

    @Override
    public String toString() {
        return "Config{" +
                "tournamentConfigPath='" + tournamentConfigPath + '\'' +
                ", telegramUrl='" + telegramUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return Objects.equals(tournamentConfigPath, config.tournamentConfigPath) &&
                Objects.equals(telegramUrl, config.telegramUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tournamentConfigPath, telegramUrl);
    }

}
