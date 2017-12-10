package sejoharp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class FindTournamentsJob {
    private static final Logger log = LoggerFactory.getLogger(FindTournamentsJob.class);

    private final PlayerConfigReader playerConfigReader;
    private final TournamentRepository repository;
    private final TournamentFinder tournamentFinder;

    @Autowired
    private FindTournamentsJob(PlayerConfigReader playerConfigReader, TournamentRepository repository, TournamentFinder tournamentFinder) {
        this.playerConfigReader = playerConfigReader;
        this.repository = repository;
        this.tournamentFinder = tournamentFinder;
    }

    public static FindTournamentsJob newFindTournamentsJob(PlayerConfigReader playerConfigReader, TournamentRepository repository, TournamentFinder tournamentFinder) {
        return new FindTournamentsJob(playerConfigReader, repository, tournamentFinder);
    }

    @Scheduled(initialDelay = 10000, fixedRate = 300000)
    public void refreshInterestingTournaments() throws IOException {
        PlayerConfig config = playerConfigReader.getPlayerConfig();
        Set<String> knownTournaments = repository.getTournaments();
        Set<String> interestingTournaments = tournamentFinder.calculateInterestingTournaments(config, knownTournaments);
        repository.replaceTournaments(interestingTournaments);
        if (interestingTournaments.isEmpty()) {
            repository.resetNotifications();
        }

        log.info("found {} interesting tournament(s): {}", interestingTournaments.size(), interestingTournaments);
    }
}
