package sejoharp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Component
public class Job {

	@Autowired
	private Downloader downloader;

	@Autowired
	private Parser parser;

	@Autowired
	private Mailer mailer;

	private List<Match> oldMatches = new ArrayList<Match>();

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Scheduled(initialDelay=30000, fixedRate = 60000)
	public void notifyPlayerForNewMatches() throws JsonParseException, JsonMappingException, IOException {
		System.out.println("The time is now " + dateFormat.format(new Date()));
		TournamentConfig tournamentConfig = getTournamentConfig();
		Document page = downloader.getPage(tournamentConfig.getUrl());
		Elements runningMatchesSnippet = parser.getRunningMatchesSnippet(page);
		List<Match> matches = parser.getMatches(runningMatchesSnippet);
		List<Match> newMatches = findAllNewMatches(matches, tournamentConfig.getPlayers());
		sendMailForNewMatches(newMatches);
	}

	private TournamentConfig getTournamentConfig() throws JsonParseException, JsonMappingException, IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("tournament.json").getFile());
		return downloader.getTournamentConfig(file);
	}

	public List<Match> findAllNewMatches(List<Match> matches, List<Player> players) {
		List<Match> newMatches = new ArrayList<>();
		for (Player player : players) {
			newMatches.addAll(findNewMatches(matches, player));
		}
		return newMatches;
	}

	private void sendMailForNewMatches(List<Match> newMatches) {
		newMatches.stream().forEach(match -> {
			try {
				MimeMessage message = mailer.createMessage(match);
				mailer.send(message);
				oldMatches.add(match);
				System.out.println("sending mail: " + match);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public List<Match> findNewMatches(List<Match> matches, Player player) {
		List<Match> newMatches = new ArrayList<>();
		for (Match match : matches) {
			if (!oldMatches.contains(match)
					&& (match.getTeam1().contains(player.getName()) || match.getTeam2().contains(player.getName()))) {
				match.setNotificationEmail(player.getEmail());
				newMatches.add(match);
			}
		}
		return newMatches;
	}

	public List<Match> getOldMatches() {
		return oldMatches;
	}
}
