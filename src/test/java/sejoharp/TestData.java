package sejoharp;

import java.util.Arrays;
import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static sejoharp.TournamentConfig.tournamentConfig;

class TestData {
	static TournamentConfig getTournament1PlayerConfig() {
		return tournamentConfig("http://tournament.com/tour",
                singletonList(new Player("User1", "user1@domain.com")));
	}

	static TournamentConfig getTournament2PlayersConfig() {
		return TournamentConfig.tournamentConfig("http://tournament.com/tour",
				asList(new Player("User1", "user1@domain.com"),
                        new Player("User2", "user2@domain.com")));
	}

	static TournamentConfig getTournament2PlayersConfig2() {
		return TournamentConfig.tournamentConfig("http://tournament.com/tour",
				asList(new Player("Userß", "user1@domain.com"),
                        new Player("User2", "user2@domain.com")));
	}

	static Match getMatch() {
		Match match = new Match();
		match.setTableNumber("2");
		match.setDisciplin("GD Vorr.");
		match.setRound("2");
		match.setTeam1("User1 / User2");
		match.setTeam2("User3 / User4");
		return match;
	}

	static Config getConig() {
		Config config = new Config();
		config.setPassword("password");
		config.setPort("587");
		config.setSenderaddress("sender@foobar.de");
		config.setSmtpserver("foobar.de");
		config.setUser("emailuser");
		config.setTournamentConfigPath("src/test/resources/tournament-test.json");
		return config;
	}
}
