package sejoharp;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static sejoharp.Config.emptyConfig;
import static sejoharp.Match.emptyMatch;
import static sejoharp.Player.createPlayer;
import static sejoharp.PlayerConfig.playerConfig;

class TestData {
    static PlayerConfig get1PlayerConfig() {
        return playerConfig(
                singletonList(createPlayer("User1", "1")));
    }

    static PlayerConfig get1PlayerConfig2() {
        return playerConfig(
                singletonList(createPlayer("Marlin Sielfeld", "1")));
    }

    static PlayerConfig get2PlayersConfig() {
        return PlayerConfig.playerConfig(
                asList(
                        createPlayer("User1", "1"),
                        createPlayer("User2", "2")));
    }

    static PlayerConfig get2PlayersConfig2() {
        return PlayerConfig.playerConfig(
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
