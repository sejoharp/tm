package sejoharp;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

@Component
public class TournamentFinderImpl implements TournamentFinder {
    private static final String LAUFENDE_TURNIERE = "laufende Turniere";
    private static final String LINK_SELECTOR = "table.table:nth-child(2) > tbody:nth-child(2) > tr > td:nth-child(2) > span:nth-child(1) > a:nth-child(1)";
    private static final String RUNNING_TOURNAMENTS_SELECTOR = "table.table:nth-child(2) > thead:nth-child(1) > tr:nth-child(1) > th:nth-child(1)";
    private final PageReader pageReader;
    private final PageReader tournamentReader;

    @Autowired
    private TournamentFinderImpl(PageReader pageReader, PageReader tournamentReader) {
        this.pageReader = pageReader;
        this.tournamentReader = tournamentReader;
    }

    static TournamentFinderImpl tournamentFinder(PageReader pageReader, PageReader tournamentReader) {
        return new TournamentFinderImpl(pageReader, tournamentReader);
    }

    public Set<String> calculateInterestingTournaments(TournamentConfig config, Set<String> knownTournaments) {
        Set<String> runningTournaments = findRunningTournaments();
        Set<String> interestingTournaments = findInterestingTournaments(config, runningTournaments);
        return runningTournaments.stream()
                .filter(url -> knownTournaments.contains(url) || interestingTournaments.contains(url))
                .collect(toSet());
    }

    Set<String> findRunningTournaments() {
        Document page = pageReader.getPage("www.tifu.info");
        if (LAUFENDE_TURNIERE.equals(page.select(RUNNING_TOURNAMENTS_SELECTOR).text())) {
            Elements runningTournaments = page.select(LINK_SELECTOR);
            return runningTournaments.stream()
                    .map(TournamentFinderImpl::composeUrl)
                    .collect(toSet());
        }
        return emptySet();
    }

    Set<String> findInterestingTournaments(TournamentConfig tournamentConfig, Set<String> runningTournaments) {
        return runningTournaments.stream()
                .filter(url -> containsPlayer(url, tournamentConfig))
                .collect(toSet());
    }

    boolean containsPlayer(String url, TournamentConfig tournamentConfig) {
        Document page = tournamentReader.getPage(url);
        String decodedPage = TournamentParserImpl.removeHtmlEncoding(page.body().text());
        for (Player player : tournamentConfig.getPlayers()) {
            if (decodedPage.contains(player.getName())) {
                return true;
            }
        }
        return false;
    }

    private static String composeUrl(Element element) {
        return element.baseUri() + element.attr("href");
    }
}
