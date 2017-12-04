package sejoharp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;
import static sejoharp.TournamentFinder.tournamentFinder;

public class TournamentFinderTest {
    @Test
    public void findsNewTournaments() throws Exception {
        // given
        TournamentConfig config = TestData.getTournament1PlayerConfig2();
        Document overviewPage = loadTournamentOverview();
        PageReader pageReader = url -> overviewPage;
        Document tournamentPage = loadTournament();
        PageReader tournamentReader = url -> tournamentPage;
        // when
        Set<String> tournaments = tournamentFinder(pageReader, tournamentReader).findNewTournaments(config);

        // then
        assertThat(tournaments, hasSize(1));
        assertThat(tournaments, hasItem("http://www.tifu.info/turnier?turnierid=177&ver=2"));
    }

    @Test
    public void findsNoTournaments() throws Exception {
        // given
        TournamentConfig config = TestData.getTournament1PlayerConfig();
        Document document = loadTournamentOverview();
        PageReader pageReader = url -> document;
        Document tournamentPage = loadTournament();
        PageReader tournamentReader = url -> tournamentPage;

        // when
        Set<String> tournaments = tournamentFinder(pageReader, tournamentReader).findNewTournaments(config);

        // then
        assertThat(tournaments, hasSize(0));
    }


    //fixtures
    private Document loadTournamentOverview() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("overview.html").getFile());
        return Jsoup.parse(file, "utf-8");
    }

    private Document loadTournament() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("tournament2.html").getFile());
        return Jsoup.parse(file, "utf-8");
    }
}