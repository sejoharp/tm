package sejoharp;

import java.net.URLDecoder;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class Parser {

	public Elements getRunningMatchesSnippet(Document doc) {
		Elements elements = doc.select("table.table:nth-child(2)");
		for (Element element : elements) {
			if (element.select("thead:nth-child(1) > tr:nth-child(1) > th:nth-child(1)").text().equals("laufende Spiele")) {
				return element.select("tbody > tr");
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
		match.setTeam1(removeHtmlEncoding(element.select("td:nth-child(4)").text()));
		match.setTeam2(removeHtmlEncoding(element.select("td:nth-child(6)").text()));
		return match;
	}

	private String removeHtmlEncoding(String htmlString){
		return htmlString.replaceAll("&nbsp"," ");
	}
}
