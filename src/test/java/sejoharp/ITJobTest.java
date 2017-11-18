package sejoharp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

public class ITJobTest {
	private Job job;
	private Parser parser;
	private Document doc;

	@Before
	public void setup() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("tournament2.html").getFile());
		doc = Jsoup.parse(file, "utf-8", "");

		parser = new Parser();
		job = new Job(null, parser, null);
	}

	@Test
	public void findsNewMatches() throws IOException {
		Elements elements = parser.getRunningMatchesSnippet(doc);
		List<Match> matches = parser.getMatches(elements);
		List<Notification> newMatches = job.findNewMatches(matches, new Player("Reimer", "ich@du.de"));
		assertThat(newMatches.get(0).getMatch().getTeam1(), containsString("Reimer"));
		assertThat(newMatches.get(0).getNotificationEmail(), is("ich@du.de"));
	}

	@Test
	public void finds2NewMatches() throws IOException {
		Elements elements = parser.getRunningMatchesSnippet(doc);
		List<Match> matches = parser.getMatches(elements);
		List<Player> players = Arrays.asList(new Player("Reimer", "ich@du.de"), new Player("Borck", "ich2@du.de"));
		List<Notification> newMatches = job.findAllNewMatches(matches, players);
		assertThat(newMatches.get(0).getMatch().getTeam1(), containsString("Reimer"));
		assertThat(newMatches.get(0).getNotificationEmail(), is("ich@du.de"));
		assertThat(newMatches.get(1).getMatch().getTeam1(), containsString("Borck"));
		assertThat(newMatches.get(1).getNotificationEmail(), is("ich2@du.de"));
		assertThat(newMatches.size(), is(2));
	}
}
