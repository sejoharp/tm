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
    private ParserImpl parser;
    private Document doc;

    @Before
    public void setup() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("tournament2.html").getFile());
        doc = Jsoup.parse(file, "utf-8", "");

        parser = new ParserImpl();
        PageReader pageReader = url -> null;
        job = Job.newJob(null, parser, pageReader, null);
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
        assertThat(newMatches.get(0).getChatId(), is("1"));
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
        assertThat(newMatches.get(0).getChatId(), is("1"));
        assertThat(newMatches.get(1).getMatch().getTeam1(), containsString("Borck"));
        assertThat(newMatches.get(1).getChatId(), is("2"));
        assertThat(newMatches.size(), is(2));
    }
}
