package sejoharp;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static sejoharp.Match.emptyMatch;

public class MatchTest {
    @Test
    public void formatsMail() {
        Match match = emptyMatch()
                .withTableNumber("2")
                .withDiscipline("GD Vorr.")
                .withRound("2")
                .withTeam1("Aron Schneider / Luciano Auria")
                .withTeam2("Sophia Becker / Benjamin Beintner");

        assertThat(match.toFormattedString()).isEqualTo(
                "Tisch:2 | Disziplin:GD Vorr. | Runde:2 | Aron Schneider / Luciano Auria vs. Sophia Becker / Benjamin Beintner");
    }
}