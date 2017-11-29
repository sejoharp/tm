package sejoharp;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static sejoharp.Config.emptyConfig;
import static sejoharp.Match.emptyMatch;
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
        return emptyMatch()
                .withTableNumber("2")
                .withDiscipline("GD Vorr.")
                .withRound("2")
                .withTeam1("User1 / User2")
                .withTeam2("User3 / User4");
    }

    static Config getConfig() {
        return emptyConfig()
                .withPassword("password")
                .withPort("587")
                .withSenderaddress("sender@foobar.de")
                .withSmtpserver("foobar.de")
                .withUser("emailuser")
                .withTournamentConfigPath("src/test/resources/tournament-test.json")
                .withTelegramToken("123:mytoken");
    }
}
