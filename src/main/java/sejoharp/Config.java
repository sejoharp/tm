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

	@Override
	public String toString() {
		return "Config [port=" + port + ", smtpserver=" + smtpserver + ", user=" + user + ", password=" + password.length()
				+ ", senderaddress=" + senderaddress + "]";
	}

}
