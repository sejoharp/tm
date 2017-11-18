package sejoharp;

import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class Parser {

	public Elements getRunningMatchesSnippet(Document doc) {
		Elements elements = doc.select("table.table:nth-child(2)");
		for (Element element : elements) {
            String tableTitle = getText(element, "thead:nth-child(1) > tr:nth-child(1) > th:nth-child(1)");
            if (tableTitle.equals("laufende Spiele")) {
				return element.select("tbody > tr");
			}
		}
		return new Elements();
	}

	List<Match> getMatches(Elements elements) {
		return elements.stream()
				.map(Parser::parseMatch)
				.collect(Collectors.toList());
	}

	private static Match parseMatch(Element element) {
		Match match = new Match();
		match.setTableNumber(getText(element, "td:nth-child(1)"));
		match.setDisciplin(getText(element, "td:nth-child(2)"));
		match.setRound(getText(element, "td:nth-child(3)"));
		match.setTeam1(removeHtmlEncoding(getText(element, "td:nth-child(4)")));
		match.setTeam2(removeHtmlEncoding(getText(element, "td:nth-child(6)")));
		return match;
	}

    private static String getText(Element element, String cssQuery) {
        return element.select(cssQuery).text();
    }

    private static String removeHtmlEncoding(String htmlString){
		return htmlString.replaceAll("&nbsp"," ");
	}
}
