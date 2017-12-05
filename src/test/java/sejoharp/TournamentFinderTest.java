package sejoharp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.emptySet;
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
        PageReader pageReader = url -> loadTournamentOverview();
        PageReader tournamentReader = url -> loadTournament();

        // when
        Set<String> tournaments = tournamentFinder(pageReader, tournamentReader).findInterestingTournaments(config);

        // then
        assertThat(tournaments).hasSize(2);
        assertThat(tournaments).containsExactly(
                "http://www.tifu.info/turnier?turnierid=177&ver=2",
                "http://www.tifu.info/turnier?turnierid=177&ver=3");
    }

    @Test
    public void findsNoTournamentsWithPlayers() throws Exception {
        // given
        PageReader pageReader = url -> loadTournamentOverview();
        PageReader tournamentReader = url -> loadTournament();
        TournamentConfig config = TestData.getTournament1PlayerConfig();

        // when
        Set<String> tournaments = tournamentFinder(pageReader, tournamentReader).findInterestingTournaments(config);

        // then
        assertThat(tournaments).hasSize(0);
    }

    @DataProvider()
    public Object[][] testdata() {
        return new Object[][]{
                //running  interesting  kown  expected
                {toSet("t1", "t2"), emptySet(), toSet("t3"), emptySet()},
                {toSet("t1", "t2"), emptySet(), toSet("t1"), toSet("t1")},
                {toSet("t1", "t2"), toSet("t2"), toSet("t1"), toSet("t1", "t2")},
                {toSet("t1", "t2"), toSet("t2"), toSet("t3"), toSet("t2")},
                {emptySet(), toSet("t2"), toSet("t3"), emptySet()},
        };
    }

    @Test(dataProvider = "testdata")
    public void findsInterestingTournaments(Set<String> running,
                                            Set<String> interesting,
                                            Set<String> known,
                                            Set<String> expected) {
        // given
        TournamentFinderImpl tournamentFinder = mock(TournamentFinderImpl.class);
        when(tournamentFinder.findRunningTournaments()).thenReturn(running);
        when(tournamentFinder.findInterestingTournaments(any())).thenReturn(interesting);
        when(tournamentFinder.calculateInterestingTournaments(any(), any())).thenCallRealMethod();

        // when
        Set<String> tournaments = tournamentFinder.calculateInterestingTournaments(null, known);

        // then
        assertThat(tournaments).containsExactlyElementsOf(expected);
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


    // helper

    private Set<String> toSet(String... urls) {
        return new HashSet<>(Arrays.asList(urls));
    }

    //fixtures

    private Document loadTournamentOverview() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("overview.html").getFile());
        try {
            return Jsoup.parse(file, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Document loadTournament() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("tournament2.html").getFile());
        try {
            return Jsoup.parse(file, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}