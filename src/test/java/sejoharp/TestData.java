package sejoharp;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static sejoharp.Player.createPlayer;
import static sejoharp.TournamentConfig.tournamentConfig;

class TestData {
    static TournamentConfig getTournament1PlayerConfig() {
        return tournamentConfig("http://tournament.com/tour",
                singletonList(createPlayer("User1", "user1@domain.com", "1")));
    }

    static TournamentConfig getTournament2PlayersConfig() {
        return TournamentConfig.tournamentConfig("http://tournament.com/tour",
                asList(
                        createPlayer("User1", "user1@domain.com", "1"),
                        createPlayer("User2", "user2@domain.com", "2")));
    }

    static TournamentConfig getTournament2PlayersConfig2() {
        return TournamentConfig.tournamentConfig("http://tournament.com/tour",
                asList(createPlayer("Userß", "user1@domain.com", "1"),
                        createPlayer("User2", "user2@domain.com", "2")));
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

    static Config getConfig() {
        Config config = new Config();
        config.setPassword("password");
        config.setPort("587");
        config.setSenderaddress("sender@foobar.de");
        config.setSmtpserver("foobar.de");
        config.setUser("emailuser");
        config.setTournamentConfigPath("src/test/resources/tournament-test.json");
//		config.setTelegramToken("123:mytoken");
        return config;
    }
}
