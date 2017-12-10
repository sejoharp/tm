package sejoharp;

import org.testng.annotations.Test;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashSet;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static sejoharp.Notification.notification;
import static sejoharp.TournamentRepository.emptyRepo;

public class FindTournamentsJobTest {
    @Test
    public void refreshesInterestingTournaments() throws IOException, MessagingException {
        // given
        PlayerConfigReader playerConfigReader = () -> TestData.get1PlayerConfig();
        String tournamentUrl = "tournamentUrl";
        TournamentFinder tournamentFinder = (config, knownTournaments) -> new HashSet<>(singletonList(tournamentUrl));
        TournamentRepository repository = emptyRepo();
        FindTournamentsJob findTournamentsJob = FindTournamentsJob.newFindTournamentsJob(playerConfigReader, repository, tournamentFinder);

        // when
        findTournamentsJob.refreshInterestingTournaments();

        // then
        assertThat(repository.getTournaments()).containsExactly(tournamentUrl);
    }

    @Test
    public void resetsSentNotificationsWhen0InterestingTournamentsFound() throws Exception {
        // given
        TournamentFinder tournamentFinder = (config, knownTournaments) -> new HashSet<>(emptyList());
        PlayerConfigReader playerConfigReader = () -> null;
        HashSet<String> interestingTournaments = new HashSet<>();
        HashSet<Notification> sentNotifications = new HashSet<>(singletonList(notification(TestData.getMatch(), "1")));
        TournamentRepository repository = TournamentRepository.repo(interestingTournaments, sentNotifications);
        FindTournamentsJob findTournamentsJob = FindTournamentsJob.newFindTournamentsJob(playerConfigReader, repository, tournamentFinder);

        // when
        findTournamentsJob.refreshInterestingTournaments();

        // then
        assertThat(repository.getSentNotificationSize()).isEqualTo(0);
    }
}
