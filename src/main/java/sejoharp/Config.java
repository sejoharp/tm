package sejoharp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		result = prime * result + ((senderaddress == null) ? 0 : senderaddress.hashCode());
		result = prime * result + ((smtpserver == null) ? 0 : smtpserver.hashCode());
		result = prime * result + ((tournamentConfigPath == null) ? 0 : tournamentConfigPath.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Config other = (Config) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (port == null) {
			if (other.port != null)
				return false;
		} else if (!port.equals(other.port))
			return false;
		if (senderaddress == null) {
			if (other.senderaddress != null)
				return false;
		} else if (!senderaddress.equals(other.senderaddress))
			return false;
		if (smtpserver == null) {
			if (other.smtpserver != null)
				return false;
		} else if (!smtpserver.equals(other.smtpserver))
			return false;
		if (tournamentConfigPath == null) {
			if (other.tournamentConfigPath != null)
				return false;
		} else if (!tournamentConfigPath.equals(other.tournamentConfigPath))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Config [port=" + port + ", smtpserver=" + smtpserver + ", user=" + user + ", password=" + password
				+ ", senderaddress=" + senderaddress + ", tournamentConfigPath=" + tournamentConfigPath + "]";
	}
}
