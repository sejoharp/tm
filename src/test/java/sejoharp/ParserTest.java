package sejoharp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;


public class ParserTest {
    private Parser parser;
    private Document doc;
    
    public static final String RECOURCES_DIRECTORY = System
            .getProperty("user.dir") + "/src/test/resources/";

    @Before
    public void setup() throws IOException{
        parser = new Parser();
    	File testFile = new File(RECOURCES_DIRECTORY + "tournament.html");
    	doc = Jsoup.parse(testFile, "UTF-8", "");
    }

    @Test
    public void getsRunningMatches() throws IOException {
        Elements elements = parser.getRunningMatchesSnippet(doc);
        assertThat(elements.size(), is(39));
    }

    @Test
    public void parseMatches() throws IOException{
        Elements elements = parser.getRunningMatchesSnippet(doc);
        List<Match> matches = parser.getMatches(elements);
        assertThat(matches.get(0).getTableNumber(), is("1"));
        assertThat(matches.get(0).getDisciplin(), is("GD Vorr."));
        assertThat(matches.get(0).getRound(), is("2"));
        assertThat(matches.get(0).getTeam1(), is("Albert Hald-Bjerrum / Peter Toubro"));
        assertThat(matches.get(0).getTeam2(), is("Michael Kunath / Maximilian Hoyer"));
    }
    
    @Test
    public void parse0Matches() throws IOException{
        Elements elements = new Elements();
        List<Match> matches = parser.getMatches(elements);
        assertThat(matches.size(), is(0));
    }
}
