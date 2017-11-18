package sejoharp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static sejoharp.Notification.notification;

@Component
public class Job {
    private final Downloader downloader;
    private final Parser parser;
    private final Mailer mailer;

    private HashSet<Match> oldMatches = new HashSet<Match>();

    private static final Logger log = LoggerFactory.getLogger(Job.class);

    @Autowired
    public Job(Downloader downloader, Parser parser, Mailer mailer) {
        super();
        this.downloader = downloader;
        this.parser = parser;
        this.mailer = mailer;
    }

    @Scheduled(initialDelay = 30000, fixedRate = 30000)
    void notifyPlayerForNewMatches() throws JsonParseException, JsonMappingException, IOException {
        log.info("reading matches..");
        TournamentConfig tournamentConfig = downloader.getTournamentConfig();
        Document page = downloader.getPage(tournamentConfig.getUrl());
        Elements runningMatchesSnippet = parser.getRunningMatchesSnippet(page);
        List<Match> matches = parser.getMatches(runningMatchesSnippet);
        List<Notification> newMatches = findAllNewMatches(matches, tournamentConfig.getPlayers());
        sendMailForNewMatches(newMatches);
    }

    List<Notification> findAllNewMatches(List<Match> matches, List<Player> players) {
        return players.stream()
                .map(player -> findNewMatches(matches, player))
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private void sendMailForNewMatches(List<Notification> notifications) {
        notifications.forEach(notification -> {
            try {
                mailer.send(mailer.createMessage(notification));
                oldMatches.add(notification.getMatch());
                log.info("sending mail: " + notification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    List<Notification> findNewMatches(List<Match> matches, Player player) {
        return matches.stream()
                .filter(match -> notifyPlayer(player, match))
                .map(match -> notification(match, player.getEmail()))
                .collect(toList());
    }

    private boolean notifyPlayer(Player player, Match match) {
        return !oldMatches.contains(match)
                && (match.getTeam1().contains(player.getName()) || match.getTeam2().contains(player.getName()));
    }
}
