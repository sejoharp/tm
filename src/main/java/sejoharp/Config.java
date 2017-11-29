package sejoharp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Component
public class Config {
    @Value("${email.port}")
    private String port;
    @Value("${email.smtpserver}")
    private String smtpserver;
    @Value("${email.user}")
    private String user;
    @Value("${email.password}")
    private String password;
    @Value("${email.senderaddress}")
    private String senderaddress;
    @Value("${tournamentConfigPath}")
    private String tournamentConfigPath;
    @Value("${telegramToken}")
    private String telegramToken;

    private Config(String port, String smtpserver, String user, String password, String senderaddress, String tournamentConfigPath, String telegramToken) {
        this.port = port;
        this.smtpserver = smtpserver;
        this.user = user;
        this.password = password;
        this.senderaddress = senderaddress;
        this.tournamentConfigPath = tournamentConfigPath;
        this.telegramToken = telegramToken;
    }

    private Config() {
    }

    public static Config emptyConfig() {
        return new Config();
    }

    public String getPassword() {
        return password;
    }

    public String getSenderaddress() {
        return senderaddress;
    }

    public String getUser() {
        return user;
    }

    public String getSmtpserver() {
        return smtpserver;
    }

    public String getPort() {
        return port;
    }

    public String getTournamentConfigPath() {
        return tournamentConfigPath;
    }

    public String getTelegramToken() {
        return telegramToken;
    }

    public Config withSenderaddress(String senderaddress) {
        return new Config(
                port,
                smtpserver,
                user,
                password,
                senderaddress,
                tournamentConfigPath,
                telegramToken);
    }

    public Config withPassword(String password) {
        return new Config(
                port,
                smtpserver,
                user,
                password,
                senderaddress,
                tournamentConfigPath,
                telegramToken);
    }

    public Config withUser(String user) {
        return new Config(
                port,
                smtpserver,
                user,
                password,
                senderaddress,
                tournamentConfigPath,
                telegramToken);
    }

    public Config withSmtpserver(String smtpserver) {
        return new Config(
                port,
                smtpserver,
                user,
                password,
                senderaddress,
                tournamentConfigPath,
                telegramToken);

    }

    public Config withPort(String port) {
        return new Config(
                port,
                smtpserver,
                user,
                password,
                senderaddress,
                tournamentConfigPath,
                telegramToken);
    }

    public Config withTournamentConfigPath(String tournamentConfigPath) {
        return new Config(
                port,
                smtpserver,
                user,
                password,
                senderaddress,
                tournamentConfigPath,
                telegramToken);
    }

    public Config withTelegramToken(String telegramToken) {
        return new Config(
                port,
                smtpserver,
                user,
                password,
                senderaddress,
                tournamentConfigPath,
                telegramToken);
    }

    @Override
    public String toString() {
        return "Config{" +
                "port='" + port + '\'' +
                ", smtpserver='" + smtpserver + '\'' +
                ", user='" + user + '\'' +
                ", password='" + (StringUtils.isEmpty(password) ? "empty" : "set") + '\'' +
                ", senderaddress='" + senderaddress + '\'' +
                ", tournamentConfigPath='" + tournamentConfigPath + '\'' +
                ", telegramToken='" + (StringUtils.isEmpty(telegramToken) ? "empty" : "set") + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return Objects.equals(port, config.port) &&
                Objects.equals(smtpserver, config.smtpserver) &&
                Objects.equals(user, config.user) &&
                Objects.equals(password, config.password) &&
                Objects.equals(senderaddress, config.senderaddress) &&
                Objects.equals(tournamentConfigPath, config.tournamentConfigPath) &&
                Objects.equals(telegramToken, config.telegramToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(port, smtpserver, user, password, senderaddress, tournamentConfigPath, telegramToken);
    }
}
