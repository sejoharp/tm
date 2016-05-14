package sejoharp;

import java.util.Arrays;

public class TestData {
	public static TournamentConfig getTournament1PlayerConfig() {
		return new TournamentConfig("http://tournament.com/tour",
				Arrays.asList(new Player("User1", "user1@domain.com")));
	}

	public static TournamentConfig getTournament2PlayersConfig() {
		return new TournamentConfig("http://tournament.com/tour",
				Arrays.asList(new Player("User1", "user1@domain.com"), new Player("User2", "user2@domain.com")));
	}

	public static Match getMatch() {
		Match match = new Match();
		match.setTableNumber("2");
		match.setDisciplin("GD Vorr.");
		match.setRound("2");
		match.setTeam1("User1 / User2");
		match.setTeam2("User3 / User4");
		return match;
	}

	public static Match getMatchWithMail() {
		Match match = new Match();
		match.setTableNumber("2");
		match.setDisciplin("GD Vorr.");
		match.setRound("2");
		match.setTeam1("User1 / User2");
		match.setTeam2("User3 / User4");
		match.setNotificationEmail("user1@domain.com");
		return match;
	}

	
	public static Config getConig() {
		Config config = new Config();
		config.setPassword("password");
		config.setPort("587");
		config.setSenderaddress("sender@foobar.de");
		config.setSmtpserver("foobar.de");
		config.setUser("emailuser");
		return config;
	}
}
