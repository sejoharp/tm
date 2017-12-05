package sejoharp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static sejoharp.Player.createPlayer;

public class JobIntegrationTest {
    private Job job;
    private TournamentParserImpl parser;
    private Document doc;

    @BeforeTest
    public void setup() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("tournament2.html").getFile());
        doc = Jsoup.parse(file, "utf-8", "");

        parser = new TournamentParserImpl();
        PageReader pageReader = url -> null;
        job = Job.newJob(null, parser, pageReader, null);
    }

    @Test
    public void findsNewMatches() throws IOException {
        // given
        List<Match> matches = parser.getMatchesFrom(doc);

        // when
        List<Notification> newMatches = job.findNewMatches(matches, createPlayer("Reimer", "1"));

        // then
        assertThat(newMatches.get(0).getMatch().getTeam1()).contains("Reimer");
        assertThat(newMatches.get(0).getChatId()).isEqualTo("1");
    }

    @Test
    public void finds2NewMatches() throws IOException {
        // given
        List<Match> matches = parser.getMatchesFrom(doc);
        List<Player> players = Arrays.asList(
                createPlayer("Reimer", "1"),
                createPlayer("Borck", "2"));

        // when
        List<Notification> newMatches = job.findAllNewMatches(matches, players);

        // then
        assertThat(newMatches.get(0).getMatch().getTeam1()).contains("Reimer");
        assertThat(newMatches.get(0).getChatId()).isEqualTo("1");
        assertThat(newMatches.get(1).getMatch().getTeam1()).contains("Borck");
        assertThat(newMatches.get(1).getChatId()).isEqualTo("2");
        assertThat(newMatches).hasSize(2);
    }
}
