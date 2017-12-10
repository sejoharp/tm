package sejoharp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static sejoharp.Notification.notification;

@Component
public class Job {
    private static final Logger log = LoggerFactory.getLogger(Job.class);

    private final PlayerConfigReader playerConfigReader;
    private final TournamentParser tournamentParser;
    private final PageReader pageReader;
    private final TelegramSender telegramSender;
    private final TournamentFinder tournamentFinder;
    private final Set<Notification> sentNotifications = new HashSet<>();
    private final TournamentRepository repository;

    @Autowired
    private Job(PlayerConfigReader playerConfigReader,
                TournamentParser tournamentParser,
                PageReader pageReader,
                TelegramSender telegramSender,
                TournamentFinder tournamentFinder,
                TournamentRepository repository) {
        this.playerConfigReader = playerConfigReader;
        this.tournamentParser = tournamentParser;
        this.pageReader = pageReader;
        this.telegramSender = telegramSender;
        this.tournamentFinder = tournamentFinder;
        this.repository = repository;
    }

    public static Job newJob(PlayerConfigReader playerConfigReader,
                             TournamentParser tournamentParser,
                             PageReader pageReader,
                             TelegramSender telegramSender,
                             TournamentFinder tournamentFinder,
                             TournamentRepository repository) {
        return new Job(playerConfigReader,
                tournamentParser,
                pageReader,
                telegramSender,
                tournamentFinder,
                repository);
    }

    @Scheduled(initialDelay = 15000, fixedRate = 10000)
    public void notifyPlayerForNewMatches() throws JsonParseException, JsonMappingException, IOException {
        PlayerConfig playerConfig = playerConfigReader.getPlayerConfig();
        repository.getTournaments().stream()
                .map(pageReader::getPage)
                .map(tournamentParser::getMatchesFrom)
                .map(matches -> findAllNewMatches(matches, playerConfig.getPlayers()))
                .flatMap(Function.identity())
                .forEach(this::notifyPlayer);
    }

    @Scheduled(initialDelay = 10000, fixedRate = 300000)
    public void refreshInterestingTournaments() throws IOException {
        PlayerConfig config = playerConfigReader.getPlayerConfig();
        Set<String> knownTournaments = repository.getTournaments();
        Set<String> interestingTournaments = tournamentFinder.calculateInterestingTournaments(config, knownTournaments);
        repository.replaceTournaments(interestingTournaments);
        if (interestingTournaments.isEmpty()){
            repository.resetNotifications();
        }

        log.info("found {} interesting tournament(s): {}", interestingTournaments.size(), interestingTournaments);
    }

    Stream<Notification> findAllNewMatches(List<Match> matches, List<Player> players) {
        return players.stream()
                .map(player -> findNewMatches(matches, player))
                .flatMap(Function.identity());
    }

    private void notifyPlayer(Notification notification) {
        try {
            telegramSender.sendMessage(notification);
            sentNotifications.add(notification);
            log.info("sending notification: " + notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Stream<Notification> findNewMatches(List<Match> matches, Player player) {
        return matches.stream()
                .filter(match -> containsRegisteredPlayer(player, match))
                .map(match -> notification(match, player.getChatId()))
                .filter(this::isNewNotification);
    }

    private boolean isNewNotification(Notification match) {
        return !sentNotifications.contains(match);
    }

    private static boolean containsRegisteredPlayer(Player player, Match match) {
        return match.getTeam1().contains(player.getName())
                || match.getTeam2().contains(player.getName());
    }
}
