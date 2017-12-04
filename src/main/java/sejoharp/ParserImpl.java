package sejoharp;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static sejoharp.Match.emptyMatch;

@Component
public class ParserImpl implements Parser {

    public static Match parseMatch(Element element) {
        return emptyMatch()
                .withTableNumber(getText(element, "td:nth-child(1)"))
                .withDiscipline(getText(element, "td:nth-child(2)"))
                .withRound(getText(element, "td:nth-child(3)"))
                .withTeam1(removeHtmlEncoding(getText(element, "td:nth-child(4)")))
                .withTeam2(removeHtmlEncoding(getText(element, "td:nth-child(6)")));
    }

    public static String getText(Element element, String cssQuery) {
        return element.select(cssQuery).text();
    }

    public static String removeHtmlEncoding(String htmlString) {
        return htmlString.replaceAll("&nbsp", " ");
    }

    @Override
    public List<Match> getMatches(Elements elements) {
        return elements.stream()
                .map(ParserImpl::parseMatch)
                .collect(Collectors.toList());
    }

}
