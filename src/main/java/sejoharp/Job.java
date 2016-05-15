package sejoharp;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private HashSet<Match> oldMatches = new HashSet<Match>();

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Scheduled(initialDelay = 30000, fixedRate = 60000)
	public void notifyPlayerForNewMatches() throws JsonParseException, JsonMappingException, IOException {
		log.info("read matches..");
		TournamentConfig tournamentConfig = downloader.getTournamentConfig();
		Document page = downloader.getPage(tournamentConfig.getUrl());
		Elements runningMatchesSnippet = parser.getRunningMatchesSnippet(page);
		List<Match> matches = parser.getMatches(runningMatchesSnippet);
		List<Notification> newMatches = findAllNewMatches(matches, tournamentConfig.getPlayers());
		sendMailForNewMatches(newMatches);
	}

	public List<Notification> findAllNewMatches(List<Match> matches, List<Player> players) {
		return players.stream()//
				.map(player -> findNewMatches(matches, player))//
				.flatMap(Collection::stream)//
				.collect(Collectors.toList());
	}

	private void sendMailForNewMatches(List<Notification> notifications) {
		notifications.stream().forEach(notification -> {
			try {
				mailer.send(mailer.createMessage(notification));
				oldMatches.add(notification.getMatch());
				log.info("sending mail: " + notification);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public List<Notification> findNewMatches(List<Match> matches, Player player) {
		return matches.stream()//
				.filter(match -> notifyPlayer(player, match))//
				.map(match -> new Notification(match, player.getEmail()))//
				.collect(Collectors.toList());
	}

	private boolean notifyPlayer(Player player, Match match) {
		return !oldMatches.contains(match)
				&& (match.getTeam1().contains(player.getName()) || match.getTeam2().contains(player.getName()));
	}
}
