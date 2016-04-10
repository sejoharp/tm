package sejoharp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.Is.is;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

public class JobTest {
	private Job job;
	private Parser parser;

	public static final String RECOURCES_DIRECTORY = System
			.getProperty("user.dir") + "/src/test/resources/";

	@Before
	public void setup() {
		parser = new Parser();
		job = new Job();
	}

	@Test
	public void findsNewMatches() throws IOException {
		Config config = new Config();
		config.setPassword("password");
		config.setPort("587");
		config.setRecipientaddress1("ich@du.de");
		config.setSearchName1("MyName");
		config.setSenderaddress("sender@foobar.de");
		config.setSmtpserver("foobar.de");
		config.setTournamentUrl("http://foobar.de");
		config.setUser("emailuser");
		
		File testFile = new File(RECOURCES_DIRECTORY + "tournament.html");
		Document doc = Jsoup.parse(testFile, "ISO-8859-1", "");

		Elements elements = parser.getRunningMatchesSnippet(doc);
		List<Match> matches = parser.getMatches(elements);
		List<Match> newMatches = job.findNewMatches(matches, "Auria",  "ich@du.de");
		assertThat(newMatches.get(0).getTeam1(), containsString("Auria"));
		assertThat(newMatches.get(0).getNotificationEmail(), is("ich@du.de"));
	}
	
	@Test
	public void finds2NewMatches() throws IOException {
		Config config = new Config();
		config.setPassword("password");
		config.setPort("587");
		config.setSearchName1("Auria");
		config.setRecipientaddress1("ich@du.de");
		config.setSearchName2("Arras");
		config.setRecipientaddress2("ich2@du.de");
		config.setSenderaddress("sender@foobar.de");
		config.setSmtpserver("foobar.de");
		config.setTournamentUrl("http://foobar.de");
		config.setUser("emailuser");
		job.setConfig(config);
		
		File testFile = new File(RECOURCES_DIRECTORY + "tournament.html");
		Document doc = Jsoup.parse(testFile, "ISO-8859-1", "");

		Elements elements = parser.getRunningMatchesSnippet(doc);
		List<Match> matches = parser.getMatches(elements);
		List<Match> newMatches = job.findAllNewMatches(matches);
		assertThat(newMatches.get(0).getTeam1(), containsString("Auria"));
		assertThat(newMatches.get(0).getNotificationEmail(), is("ich@du.de"));
		assertThat(newMatches.get(1).getTeam1(), containsString("Arras"));
		assertThat(newMatches.get(1).getNotificationEmail(), is("ich2@du.de"));
		assertThat(newMatches.size(), is(2));
	}
	
	
	@Test
	public void doesNotFindOldMatches() throws IOException {
		File testFile = new File(RECOURCES_DIRECTORY + "tournament.html");
		Document doc = Jsoup.parse(testFile, "ISO-8859-1", "");

		Match match = new Match();
		match.setTableNumber("2");
		match.setDisciplin("GD Vorr.");
		match.setRound("2");
		match.setTeam1("Aron Schneider / Luciano Auria");
		match.setTeam2("Sophia Becker / Benjamin Beintner");
		job.getOldMatches().add(match);
		
		Elements elements = parser.getRunningMatchesSnippet(doc);
		List<Match> matches = parser.getMatches(elements);
		List<Match> newMatches = job.findNewMatches(matches, "Auria", "ich@du.de");
		assertThat(newMatches.size(), is(0));
	}
}
