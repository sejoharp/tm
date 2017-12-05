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
                singletonList(createPlayer("User1", "1")));
    }

    static TournamentConfig getTournament1PlayerConfig2() {
        return tournamentConfig("http://tournament.com/tour",
                singletonList(createPlayer("Marlin Sielfeld", "1")));
    }

    static TournamentConfig getTournament2PlayersConfig() {
        return TournamentConfig.tournamentConfig("http://tournament.com/tour",
                asList(
                        createPlayer("User1", "1"),
                        createPlayer("User2", "2")));
    }

    static TournamentConfig getTournament2PlayersConfig2() {
        return TournamentConfig.tournamentConfig("http://tournament.com/tour",
                asList(createPlayer("Userß", "1"),
                        createPlayer("User2", "2")));
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
                .withTournamentConfigPath("src/test/resources/tournament-test.json")
                .withTelegramUrl("https://api.telegram.local/bot123:mytoken");
    }
}
