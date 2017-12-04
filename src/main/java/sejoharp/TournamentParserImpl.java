package sejoharp;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static sejoharp.Match.emptyMatch;

@Component
public class TournamentParserImpl implements TournamentParser {

    @Override
    public List<Match> getMatchesFrom(Document doc) {
        Elements elements = getRunningMatchesSnippet(doc);
        return getMatches(elements);
    }

    List<Match> getMatches(Elements elements) {
        return elements.stream()
                .map(TournamentParserImpl::parseMatch)
                .collect(Collectors.toList());
    }

    static Elements getRunningMatchesSnippet(Document doc) {
        Elements elements = doc.select("table.table:nth-child(2)");
        for (Element element : elements) {
            String tableTitle = getText(element, "thead:nth-child(1) > tr:nth-child(1) > th:nth-child(1)");
            if (tableTitle.equals("laufende Spiele")) {
                return element.select("tbody > tr");
            }
        }
        return new Elements();
    }

    private static Match parseMatch(Element element) {
        return emptyMatch()
                .withTableNumber(getText(element, "td:nth-child(1)"))
                .withDiscipline(getText(element, "td:nth-child(2)"))
                .withRound(getText(element, "td:nth-child(3)"))
                .withTeam1(removeHtmlEncoding(getText(element, "td:nth-child(4)")))
                .withTeam2(removeHtmlEncoding(getText(element, "td:nth-child(6)")));
    }

    private static String getText(Element element, String cssQuery) {
        return element.select(cssQuery).text();
    }

    public static String removeHtmlEncoding(String htmlString) {
        return htmlString.replaceAll("&nbsp", " ");
    }

}
