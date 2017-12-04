package sejoharp;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public interface Parser {

    default Elements getRunningMatchesSnippet(Document doc) {
        Elements elements = doc.select("table.table:nth-child(2)");
        for (Element element : elements) {
            String tableTitle = ParserImpl.getText(element, "thead:nth-child(1) > tr:nth-child(1) > th:nth-child(1)");
            if (tableTitle.equals("laufende Spiele")) {
                return element.select("tbody > tr");
            }
        }
        return new Elements();
    }

    List<Match> getMatches(Elements elements);
}
