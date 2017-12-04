package sejoharp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TournamentParserTest {
    private TournamentParserImpl parser;
    private Document doc;

    @Before
    public void setup() throws IOException {
        parser = new TournamentParserImpl();
        ClassLoader classLoader = getClass().getClassLoader();
        File testFile = new File(classLoader.getResource("tournament2.html").getFile());
        doc = Jsoup.parse(testFile, "utf-8", "");
    }

    @Test
    public void getsRunningMatches() throws IOException {
        Elements elements = TournamentParserImpl.getRunningMatchesSnippet(doc);
        assertThat(elements.size(), is(8));
    }

    @Test
    public void parsesMatches() throws IOException {
        // when
        List<Match> matches = parser.getMatchesFrom(doc);

        // then
        Match first = matches.get(0);
        assertThat(first.getTableNumber(), is("1"));
        assertThat(first.getDiscipline(), is("MD Profi"));
        assertThat(first.getRound(), is("1/8"));
        assertThat(first.getTeam1(), is("Mia Reimer / Arne Borck"));
        assertThat(first.getTeam2(), is("Michael Strauß / Petra Andres"));
    }

    @Test
    public void parseSpecialMatches() throws IOException {
        List<Match> matches = parser.getMatchesFrom(doc);
        assertThat(matches.get(2).getTeam1(), is("Niclas Grote / Mona Brandt"));
    }

    @Test
    public void parse0Matches() throws IOException {
        Elements elements = new Elements();
        List<Match> matches = parser.getMatches(elements);
        assertThat(matches.size(), is(0));
    }
}
