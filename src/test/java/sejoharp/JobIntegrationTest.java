package sejoharp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static sejoharp.Player.createPlayer;

public class JobIntegrationTest {
    private Job job;
    private Parser parser;
    private Document doc;

    @Before
    public void setup() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("tournament2.html").getFile());
        doc = Jsoup.parse(file, "utf-8", "");

        parser = new Parser();
        PageReader pageReader = url -> null;
        job = new Job(null, parser, null, pageReader);
    }

    @Test
    public void findsNewMatches() throws IOException {
        // given
        Elements elements = parser.getRunningMatchesSnippet(doc);
        List<Match> matches = parser.getMatches(elements);

        // when
        List<Notification> newMatches = job.findNewMatches(matches, createPlayer("Reimer", "ich@du.de", "1"));

        // then
        assertThat(newMatches.get(0).getMatch().getTeam1(), containsString("Reimer"));
        assertThat(newMatches.get(0).getEmail(), is("ich@du.de"));
    }

    @Test
    public void finds2NewMatches() throws IOException {
        // given
        Elements elements = parser.getRunningMatchesSnippet(doc);
        List<Match> matches = parser.getMatches(elements);
        List<Player> players = Arrays.asList(
                createPlayer("Reimer", "ich@du.de", "1"),
                createPlayer("Borck", "ich2@du.de", "2"));

        // when
        List<Notification> newMatches = job.findAllNewMatches(matches, players);

        // then
        assertThat(newMatches.get(0).getMatch().getTeam1(), containsString("Reimer"));
        assertThat(newMatches.get(0).getEmail(), is("ich@du.de"));
        assertThat(newMatches.get(1).getMatch().getTeam1(), containsString("Borck"));
        assertThat(newMatches.get(1).getEmail(), is("ich2@du.de"));
        assertThat(newMatches.size(), is(2));
    }
}
