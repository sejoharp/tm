package sejoharp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.internet.MimeMessage;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Job {
	
	@Autowired
	private Config config;
	
	@Autowired
	Downloader downloader;

	@Autowired
	Parser parser;

	@Autowired
	Mailer mailer;

	private List<Match> oldMatches = new ArrayList<Match>();

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"HH:mm:ss");

	@Scheduled(fixedRate = 60000)
	public void notifyPlayerForNewGames() {
		System.out.println("The time is now " + dateFormat.format(new Date()));

		List<Match> newMatches = getNewMatches();
		List<Player> players = new ArrayList<Player>();
		sendMailForNewMatches(newMatches, players);
	}

	private List<Match> getNewMatches() {
		Document page = downloader.getPage(config.getTournamentUrl());
		Elements runningMatchesSnippet = parser.getRunningMatchesSnippet(page);
		List<Match> matches = parser.getMatches(runningMatchesSnippet);
		List<Match> newMatches = findNewMatches(matches, config.getSearchName());
		return newMatches;
	}

	private void sendMailForNewMatches(List<Match> newMatches, List<Player> players) {
		newMatches.stream().forEach(match -> {
			try {
				MimeMessage message = mailer.createMessage(match);
				mailer.send(message);
				System.out.println("sending mail: " + match);
				oldMatches.add(match);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public List<Match> findNewMatches(List<Match> matches, String name) {
		return matches
				.stream()
				.filter(match -> match.getTeam1().contains(name)
						|| match.getTeam2().contains(name))//
				.filter(match -> !oldMatches.contains(match))//
				.collect(Collectors.toList());
	}

	public List<Match> getOldMatches() {
		return oldMatches;
	}
}
