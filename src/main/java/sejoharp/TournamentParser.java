package sejoharp;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public interface TournamentParser {

    List<Match> getMatchesFrom(Document doc);
}
