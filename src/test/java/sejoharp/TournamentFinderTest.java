package sejoharp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static sejoharp.TournamentFinderImpl.tournamentFinder;

public class TournamentFinderTest {
    @Test
    public void findsTournamentsWithPlayers() throws Exception {
        // given
        TournamentConfig config = TestData.getTournament1PlayerConfig2();
        Document overviewPage = loadTournamentOverview();
        PageReader pageReader = url -> overviewPage;
        Document tournamentPage = loadTournament();
        PageReader tournamentReader = url -> tournamentPage;

        // when
        Set<String> tournaments = tournamentFinder(pageReader, tournamentReader).findTournamentsWithPlayers(config);

        // then
        assertThat(tournaments).hasSize(2);
        assertThat(tournaments).containsExactly(
                "http://www.tifu.info/turnier?turnierid=177&ver=2",
                "http://www.tifu.info/turnier?turnierid=177&ver=3");
    }

    @Test
    public void findsNoTournamentsWithPlayers() throws Exception {
        // given
        Document document = loadTournamentOverview();
        PageReader pageReader = url -> document;
        Document tournamentPage = loadTournament();

        // when
        TournamentConfig config = TestData.getTournament1PlayerConfig();
        PageReader tournamentReader = url -> tournamentPage;
        Set<String> tournaments = tournamentFinder(pageReader, tournamentReader).findTournamentsWithPlayers(config);

        // then
        assertThat(tournaments).hasSize(0);
    }

    @Test()
    public void removeEndedTournamentsWithPlayers(Set<String> running,
                                                  Set<String> interesting,
                                                  Set<String> known,
                                                  Set<String> expected) {
        // given
        TournamentFinder tournamentFinder = mock(TournamentFinder.class);
        when(tournamentFinder.findRunningTournaments()).thenReturn(running);
        when(tournamentFinder.findTournamentsWithPlayers(any())).thenReturn(interesting);
        when(tournamentFinder.calculateInterestingTournaments(any(), any())).thenCallRealMethod();

        // when
        Set<String> tournaments = tournamentFinder.calculateInterestingTournaments(null, known);

        // then
        assertThat(tournaments).containsExactlyElementsOf(expected);
    }

    @Test
    public void addNewRunningTournamentsWithPlayers() throws Exception {

    }

    @Test
    public void findsRunningTournaments() throws Exception {
        // given
        Document overviewPage = loadTournamentOverview();
        PageReader pageReader = url -> overviewPage;

        // when
        Set<String> tournaments = tournamentFinder(pageReader, null).findRunningTournaments();

        // then
        assertThat(tournaments).hasSize(2);
        assertThat(tournaments).containsExactly(
                "http://www.tifu.info/turnier?turnierid=177&ver=2",
                "http://www.tifu.info/turnier?turnierid=177&ver=3");
    }


    //fixtures

    private HashSet<String> runningTournaments() {
        return new HashSet<>(Arrays.asList(
                "http://www.tifu.info/turnier?turnierid=179&ver=1",
                "http://www.tifu.info/turnier?turnierid=179&ver=2"));
    }

    private HashSet<String> knownTournaments() {
        return new HashSet<>(Arrays.asList(
                "http://www.tifu.info/turnier?turnierid=179&ver=1",
                "http://www.tifu.info/turnier?turnierid=179&ver=3"));
    }

    private HashSet<String> tournamentsWithPlayers() {
        return new HashSet<>(Arrays.asList(
                "http://www.tifu.info/turnier?turnierid=179&ver=1",
                "http://www.tifu.info/turnier?turnierid=179&ver=3"));
    }

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