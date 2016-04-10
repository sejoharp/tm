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
	@Value("${email.recipientaddress1}")
	private String recipientaddress1;
	@Value("${email.recipientaddress2}")
	private String recipientaddress2;
	@Value("${tournamentUrl}")
	private String tournamentUrl;
	@Value("${searchName1}")
	private String searchName1;
	@Value("${searchName2}")
	private String searchName2;

	public String getRecipientaddress1() {
		return recipientaddress1;
	}

	public void setRecipientaddress1(String recipientaddress1) {
		this.recipientaddress1 = recipientaddress1;
	}

	public String getRecipientaddress2() {
		return recipientaddress2;
	}

	public void setRecipientaddress2(String recipientaddress2) {
		this.recipientaddress2 = recipientaddress2;
	}

	public String getSearchName1() {
		return searchName1;
	}

	public void setSearchName1(String searchName1) {
		this.searchName1 = searchName1;
	}

	public String getSearchName2() {
		return searchName2;
	}

	public void setSearchName2(String searchName2) {
		this.searchName2 = searchName2;
	}

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

	public String getTournamentUrl() {
		return tournamentUrl;
	}

	public void setTournamentUrl(String tournamentUrl) {
		this.tournamentUrl = tournamentUrl;
	}

	@Override
	public String toString() {
		return "Config [port=" + port + ", smtpserver=" + smtpserver
				+ ", user=" + user + ", senderaddress=" + senderaddress
				+ ", recipientaddress1=" + recipientaddress1
				+ ", recipientaddress2=" + recipientaddress2
				+ ", tournamentUrl=" + tournamentUrl + ", searchName1="
				+ searchName1 + ", searchName2=" + searchName2 + "]";
	}
}
