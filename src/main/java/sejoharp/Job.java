package sejoharp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
    private final TournamentFinder tournamentFinder;
    private final Set<Match> sentNotifications = new HashSet<>();
    private Set<String> interestingTournaments;

    @Autowired
    private Job(TournamentConfigReader tournamentConfigReader,
                TournamentParser tournamentParser,
                PageReader pageReader,
                TelegramSender telegramSender,
                TournamentFinder tournamentFinder) {
        this(tournamentConfigReader,
                tournamentParser,
                pageReader,
                telegramSender,
                tournamentFinder,
                new HashSet<>());
    }

    private Job(TournamentConfigReader tournamentConfigReader,
                TournamentParser tournamentParser,
                PageReader pageReader,
                TelegramSender telegramSender,
                TournamentFinder tournamentFinder,
                Set<String> interestingTournaments) {
        this.tournamentConfigReader = tournamentConfigReader;
        this.tournamentParser = tournamentParser;
        this.pageReader = pageReader;
        this.telegramSender = telegramSender;
        this.tournamentFinder = tournamentFinder;
        this.interestingTournaments = interestingTournaments;
    }

    public static Job newJob(TournamentConfigReader tournamentConfigReader,
                             TournamentParser tournamentParser,
                             PageReader pageReader,
                             TelegramSender telegramSender,
                             TournamentFinder tournamentFinder,
                             Set<String> interestingTournaments) {
        return new Job(tournamentConfigReader,
                tournamentParser,
                pageReader,
                telegramSender,
                tournamentFinder,
                interestingTournaments);
    }

    @Scheduled(initialDelay = 15000, fixedRate = 10000)
    public void notifyPlayerForNewMatches() throws JsonParseException, JsonMappingException, IOException {
        log.info("reading matches...");

        TournamentConfig tournamentConfig = tournamentConfigReader.getTournamentConfig();
        interestingTournaments.stream()
                .map(pageReader::getPage)
                .map(tournamentParser::getMatchesFrom)
                .map(matches -> findAllNewMatches(matches, tournamentConfig.getPlayers()))
                .forEach(this::notifyPlayer);
    }

    @Scheduled(initialDelay = 10000, fixedRate = 300000)
    public void refreshInterestingTournaments() throws IOException {
        log.info("looking for new tournaments...");

        TournamentConfig config = tournamentConfigReader.getTournamentConfig();
        Set<String> interestingTournaments = tournamentFinder.calculateInterestingTournaments(config, this.interestingTournaments);
        this.interestingTournaments = interestingTournaments;

        log.info("found {} interesting tournament(s): {}", interestingTournaments.size(), interestingTournaments);
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

    private static boolean containsRegisteredPlayer(Player player, Match match) {
        return match.getTeam1().contains(player.getName())
                || match.getTeam2().contains(player.getName());
    }
}
