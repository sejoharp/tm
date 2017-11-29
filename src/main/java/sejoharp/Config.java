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

	public String getSenderaddress() {
		return senderaddress;
	}

	public void setSenderaddress(String senderaddress) {
		this.senderaddress = senderaddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getSmtpserver() {
		return smtpserver;
	}

	public void setSmtpserver(String smtpserver) {
		this.smtpserver = smtpserver;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getTournamentConfigPath() {
		return tournamentConfigPath;
	}

	public void setTournamentConfigPath(String tournamentConfigPath) {
		this.tournamentConfigPath = tournamentConfigPath;
	}


    public String getTelegramToken() {
        return telegramToken;
    }

	@Override
	public String toString() {
		return "Config{" +
				"port='" + port + '\'' +
				", smtpserver='" + smtpserver + '\'' +
				", user='" + user + '\'' +
				", password='" + (StringUtils.isEmpty(password) ? "empty":"set") + '\'' +
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

    public void setTelegramToken(String telegramToken) {
        this.telegramToken = telegramToken;
    }
}
