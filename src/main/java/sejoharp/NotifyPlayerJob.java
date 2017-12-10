package sejoharp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static sejoharp.Notification.notification;

@Component
public class NotifyPlayerJob {
    private static final Logger log = LoggerFactory.getLogger(NotifyPlayerJob.class);

    private final PlayerConfigReader playerConfigReader;
    private final TournamentParser tournamentParser;
    private final PageReader pageReader;
    private final TelegramSender telegramSender;
    private final TournamentRepository repository;

    @Autowired
    private NotifyPlayerJob(PlayerConfigReader playerConfigReader,
                            TournamentParser tournamentParser,
                            PageReader pageReader,
                            TelegramSender telegramSender,
                            TournamentRepository repository) {
        this.playerConfigReader = playerConfigReader;
        this.tournamentParser = tournamentParser;
        this.pageReader = pageReader;
        this.telegramSender = telegramSender;
        this.repository = repository;
    }

    public static NotifyPlayerJob newNotifyPlayerJob(PlayerConfigReader playerConfigReader,
                                                     TournamentParser tournamentParser,
                                                     PageReader pageReader,
                                                     TelegramSender telegramSender,
                                                     TournamentRepository repository) {
        return new NotifyPlayerJob(playerConfigReader,
                tournamentParser,
                pageReader,
                telegramSender,
                repository);
    }

    @Scheduled(initialDelay = 15000, fixedRate = 10000)
    public void notifyPlayerForNewMatches() throws JsonParseException, JsonMappingException, IOException {
        PlayerConfig playerConfig = playerConfigReader.getPlayerConfig();
        repository.getTournaments().stream()
                .map(pageReader::getPage)
                .map(tournamentParser::getMatchesFrom)
                .map(matches -> findAllNewNotifications(matches, playerConfig.getPlayers()))
                .flatMap(Function.identity())
                .forEach(this::notifyPlayer);
    }

    Stream<Notification> findAllNewNotifications(List<Match> matches, List<Player> players) {
        return players.stream()
                .map(player -> findNewNotifications(matches, player))
                .flatMap(Function.identity());
    }

    Stream<Notification> findNewNotifications(List<Match> matches, Player player) {
        return matches.stream()
                .filter(match -> containsRegisteredPlayer(player, match))
                .map(match -> notification(match, player.getChatId()))
                .filter(repository::isNewNotification);
    }

    private void notifyPlayer(Notification notification) {
        try {
            telegramSender.sendMessage(notification);
            repository.addNotification(notification);
            log.info("sending notification: " + notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean containsRegisteredPlayer(Player player, Match match) {
        return match.getTeam1().contains(player.getName())
                || match.getTeam2().contains(player.getName());
    }
}
