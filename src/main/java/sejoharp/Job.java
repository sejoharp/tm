package sejoharp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.internet.MimeMessage;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
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

	@Autowired
    private ResourceLoader resourceLoader;
	
	private HashSet<Match> oldMatches = new HashSet<Match>();

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Scheduled(initialDelay=30000, fixedRate = 60000)
	public void notifyPlayerForNewMatches() throws JsonParseException, JsonMappingException, IOException {
		log.info("nothing to do...");
		TournamentConfig tournamentConfig = getTournamentConfig();
		Document page = downloader.getPage(tournamentConfig.getUrl());
		Elements runningMatchesSnippet = parser.getRunningMatchesSnippet(page);
		List<Match> matches = parser.getMatches(runningMatchesSnippet);
		List<Notification> newMatches = findAllNewMatches(matches, tournamentConfig.getPlayers());
		sendMailForNewMatches(newMatches);
	}

	private TournamentConfig getTournamentConfig() throws JsonParseException, JsonMappingException, IOException {
		InputStream file = resourceLoader.getResource("classpath:tournament.json").getInputStream();
		return downloader.getTournamentConfig(file);
	}

	public List<Notification> findAllNewMatches(List<Match> matches, List<Player> players) {
		List<Notification> allNewMatches = new ArrayList<>();
		for (Player player : players) {
			allNewMatches.addAll(findNewMatches(matches, player));
		}
		return allNewMatches;
	}

	private void sendMailForNewMatches(List<Notification> notifications) {
		notifications.stream().forEach(notification -> {
			try {
				MimeMessage message = mailer.createMessage(notification);
				mailer.send(message);
				oldMatches.add(notification.getMatch());
				log.info("sending mail: " + notification);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public List<Notification> findNewMatches(List<Match> matches, Player player) {
		List<Notification> newMatches = new ArrayList<>();
		for (Match match : matches) {
			if (!oldMatches.contains(match)
					&& (match.getTeam1().contains(player.getName()) || match.getTeam2().contains(player.getName()))) {
				newMatches.add(new Notification(match, player.getEmail()));
			}
		}
		return newMatches;
	}

	public Set<Match> getOldMatches() {
		return oldMatches;
	}
}
