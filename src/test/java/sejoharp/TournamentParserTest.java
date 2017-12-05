package sejoharp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TournamentParserTest {
    private TournamentParserImpl parser;
    private Document doc;

    @BeforeTest
    public void setup() throws IOException {
        parser = new TournamentParserImpl();
        ClassLoader classLoader = getClass().getClassLoader();
        File testFile = new File(classLoader.getResource("tournament2.html").getFile());
        doc = Jsoup.parse(testFile, "utf-8", "");
    }

    @Test
    public void getsRunningMatches() throws IOException {
        Elements elements = TournamentParserImpl.getRunningMatchesSnippet(doc);
        assertThat(elements).hasSize(8);
    }

    @Test
    public void parsesMatches() throws IOException {
        // when
        List<Match> matches = parser.getMatchesFrom(doc);

        // then
        Match first = matches.get(0);
        assertThat(first.getTableNumber()).isEqualTo("1");
        assertThat(first.getDiscipline()).isEqualTo("MD Profi");
        assertThat(first.getRound()).isEqualTo("1/8");
        assertThat(first.getTeam1()).isEqualTo("Mia Reimer / Arne Borck");
        assertThat(first.getTeam2()).isEqualTo("Michael Strauß / Petra Andres");
    }

    @Test
    public void parseSpecialMatches() throws IOException {
        List<Match> matches = parser.getMatchesFrom(doc);
        assertThat(matches.get(2).getTeam1()).isEqualTo("Niclas Grote / Mona Brandt");
    }

    @Test
    public void parse0Matches() throws IOException {
        Elements elements = new Elements();
        List<Match> matches = parser.getMatches(elements);
        assertThat(matches).isEmpty();
    }
}
