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

public class JobTest {
	private Job job;
	private Parser parser;
	private Document doc;
	private Config config;

	@Before
	public void setup() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("tournament.html").getFile());
		doc = Jsoup.parse(file, "ISO-8859-1", "");

		config = new Config();
		config.setPassword("password");
		config.setPort("587");
		config.setSenderaddress("sender@foobar.de");
		config.setSmtpserver("foobar.de");
		config.setUser("emailuser");
		parser = new Parser();
		job = new Job();
	}

	@Test
	public void findsNewMatches() throws IOException {
		Elements elements = parser.getRunningMatchesSnippet(doc);
		List<Match> matches = parser.getMatches(elements);
		List<Match> newMatches = job.findNewMatches(matches, new Player("Auria", "ich@du.de"));
		assertThat(newMatches.get(0).getTeam1(), containsString("Auria"));
		assertThat(newMatches.get(0).getNotificationEmail(), is("ich@du.de"));
	}

	@Test
	public void finds2NewMatches() throws IOException {
		Elements elements = parser.getRunningMatchesSnippet(doc);
		List<Match> matches = parser.getMatches(elements);
		List<Player> players = Arrays.asList(new Player("Auria", "ich@du.de"), new Player("Arras", "ich2@du.de"));
		List<Match> newMatches = job.findAllNewMatches(matches, players);
		assertThat(newMatches.get(0).getTeam1(), containsString("Auria"));
		assertThat(newMatches.get(0).getNotificationEmail(), is("ich@du.de"));
		assertThat(newMatches.get(1).getTeam1(), containsString("Arras"));
		assertThat(newMatches.get(1).getNotificationEmail(), is("ich2@du.de"));
		assertThat(newMatches.size(), is(2));
	}
}
