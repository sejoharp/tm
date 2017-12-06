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
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static sejoharp.Notification.notification;

@Component
public class Job {
    private static final Logger log = LoggerFactory.getLogger(Job.class);
    private final TournamentConfigReader tournamentConfigReader;
    private final TournamentParser tournamentParser;
    private final PageReader pageReader;
    private final TelegramSender telegramSender;
    private Set<Match> sentNotifications = new HashSet<>();

    @Autowired
    private Job(TournamentConfigReader tournamentConfigReader,
                TournamentParser tournamentParser,
                PageReader pageReader,
                TelegramSender telegramSender) {
        this.tournamentConfigReader = tournamentConfigReader;
        this.tournamentParser = tournamentParser;
        this.pageReader = pageReader;
        this.telegramSender = telegramSender;
    }

    public static Job newJob(TournamentConfigReader tournamentConfigReader,
                             TournamentParser tournamentParser,
                             PageReader pageReader,
                             TelegramSender telegramSender){
        return new Job(tournamentConfigReader, tournamentParser, pageReader, telegramSender);
    }

    @Scheduled(initialDelay = 10000, fixedRate = 10000)
    public void notifyPlayerForNewMatches() throws JsonParseException, JsonMappingException, IOException {
        log.info("reading matches..");
        TournamentConfig tournamentConfig = tournamentConfigReader.getTournamentConfig();
        Document page = pageReader.getPage(tournamentConfig.getUrl());
        List<Match> matches = tournamentParser.getMatchesFrom(page);
        List<Notification> newMatches = findAllNewMatches(matches, tournamentConfig.getPlayers());
        notifyPlayer(newMatches);
    }

    List<Notification> findAllNewMatches(List<Match> matches, List<Player> players) {
        return players.stream()
                .map(player -> findNewMatches(matches, player))
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private void notifyPlayer(List<Notification> notifications) {
        notifications.forEach(notification -> {
            try {
                telegramSender.sendMessage(notification);
                sentNotifications.add(notification.getMatch());
                log.info("sending notification: " + notification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    List<Notification> findNewMatches(List<Match> matches, Player player) {
        return matches.stream()
                .filter(match -> containsRegisteredPlayer(player, match))
                .filter(this::isNewNotification)
                .map(match -> notification(match, player.getChatId()))
                .collect(toList());
    }

    private boolean isNewNotification(Match match) {
        return !sentNotifications.contains(match);
    }

    private boolean containsRegisteredPlayer(Player player, Match match) {
        return match.getTeam1().contains(player.getName())
                || match.getTeam2().contains(player.getName());
    }
}
