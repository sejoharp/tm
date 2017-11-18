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

	@Before
	public void setup() throws IOException {
		parser = new Parser();
		ClassLoader classLoader = getClass().getClassLoader();
		File testFile = new File(classLoader.getResource("tournament2.html").getFile());
		doc = Jsoup.parse(testFile, "utf-8", "");
	}

	@Test
	public void getsRunningMatches() throws IOException {
		Elements elements = parser.getRunningMatchesSnippet(doc);
		assertThat(elements.size(), is(8));
	}

	@Test
	public void parsesMatches() throws IOException {
		Elements elements = parser.getRunningMatchesSnippet(doc);
		List<Match> matches = parser.getMatches(elements);
		assertThat(matches.get(0).getTableNumber(), is("1"));
		assertThat(matches.get(0).getDisciplin(), is("MD Profi"));
		assertThat(matches.get(0).getRound(), is("1/8"));
		assertThat(matches.get(0).getTeam1(), is("Mia Reimer / Arne Borck"));
		assertThat(matches.get(0).getTeam2(), is("Michael Strauß / Petra Andres"));
	}

	@Test
	public void parseSpecialMatches() throws IOException {
		Elements elements = parser.getRunningMatchesSnippet(doc);
		List<Match> matches = parser.getMatches(elements);
		assertThat(matches.get(2).getTeam1(), is("Niclas Grote / Mona Brandt"));
	}

	@Test
	public void parse0Matches() throws IOException {
		Elements elements = new Elements();
		List<Match> matches = parser.getMatches(elements);
		assertThat(matches.size(), is(0));
	}
}
