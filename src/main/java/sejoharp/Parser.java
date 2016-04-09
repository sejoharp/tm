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
		Elements elements = doc.select("div#tabelle > table");
		for (Element element : elements) {
			if (element.select("caption:contains(laufende Spiele").size() == 1) {
				return element.select("tbody > tr:not(:first-child)");
			}
		}
		return new Elements();
	}

	public List<Match> getMatches(Elements elements) {
		return elements.stream().map(match -> {
			return parseMatch(match);
		}).collect(Collectors.toList());
	}

	private Match parseMatch(Element element) {
		Match match = new Match();
		match.setTableNumber(element.select("td:nth-child(1)").text());
		match.setDisciplin(element.select("td:nth-child(2)").text());
		match.setRound(element.select("td:nth-child(3)").text());
		match.setTeam1(element.select("td:nth-child(4)").text());
		match.setTeam2(element.select("td:nth-child(6)").text());
		return match;
	}
}
