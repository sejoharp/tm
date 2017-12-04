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
public class TournamentFinder {
    public static final String LAUFENDE_TURNIERE = "laufende Turniere";
    private final PageReader pageReader;
    private final PageReader tournamentReader;
    private static final String LINK_SELECTOR = "table.table:nth-child(2) > tbody:nth-child(2) > tr > td:nth-child(2) > span:nth-child(1) > a:nth-child(1)";
    private static final String RUNNING_TOURNAMENTS_SELECTOR = "table.table:nth-child(2) > thead:nth-child(1) > tr:nth-child(1) > th:nth-child(1)";

    @Autowired
    private TournamentFinder(PageReader pageReader, PageReader tournamentReader) {
        this.pageReader = pageReader;
        this.tournamentReader = tournamentReader;
    }

    public static TournamentFinder tournamentFinder(PageReader pageReader, PageReader tournamentReader) {
        return new TournamentFinder(pageReader, tournamentReader);
    }

    public Set<String> findNewTournaments(TournamentConfig tournamentConfig) {
        Document page = pageReader.getPage("www.tifu.info");
        if (LAUFENDE_TURNIERE.equals(page.select(RUNNING_TOURNAMENTS_SELECTOR).text())) {
            Elements runningTournaments = page.select(LINK_SELECTOR);
            return runningTournaments.stream()
                    .map(TournamentFinder::getUrl)
                    .filter(url -> containsPlayer(url, tournamentConfig))
                    .collect(toSet());
        }
        return emptySet();
    }

    private static String getUrl(Element element) {
        return element.baseUri() + element.attr("href");
    }

    private boolean containsPlayer(String url, TournamentConfig tournamentConfig) {
        Document page = tournamentReader.getPage(url);
        String decodedPage = TournamentParserImpl.removeHtmlEncoding(page.body().text());
        for (Player player : tournamentConfig.getPlayers()) {
            if (decodedPage.contains(player.getName())) {
                return true;
            }
        }
        return false;
    }
}
