package sejoharp;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static sejoharp.Notification.notification;
import static sejoharp.TournamentRepository.emptyRepo;
import static sejoharp.TournamentRepository.repo;

public class TournamentRepositoryTest {
    @Test
    public void getsNotificationsSize() throws Exception {
        // given
        TournamentRepository repository = emptyRepo();

        // when
        int sentNotificationsSize = repository.getSentNotificationSize();

        // then
        assertThat(sentNotificationsSize).isEqualTo(0);
    }

    @Test
    public void addsNotification() throws Exception {
        // given
        TournamentRepository repository = emptyRepo();
        Notification notification = notification(TestData.getMatch(), "1");

        // when
        repository.addNotification(notification);

        // then
        assertThat(repository.getSentNotificationSize()).isEqualTo(1);
    }

    @Test
    public void getsNotifications() throws Exception {
        // given
        Notification notification = notification(TestData.getMatch(), "1");
        TournamentRepository repository = repo(new HashSet<>(), new HashSet<>(Collections.singletonList(notification)));

        // when
        Set<Notification> notifications = repository.getNotifications();

        // then
        assertThat(notifications).containsExactly(notification);
    }

    @Test
    public void resetsNotifications() throws Exception {
        // given
        Notification notification = notification(TestData.getMatch(), "1");
        TournamentRepository repository = repo(new HashSet<>(), new HashSet<>(Collections.singletonList(notification)));

        // when
        repository.resetNotifications();

        // then
        assertThat(repository.getSentNotificationSize()).isEqualTo(0);
    }

    @Test
    public void getsTournamentsSize() throws Exception {
        // given
        TournamentRepository repository = emptyRepo();

        // when
        int size = repository.getTournementSize();

        // then
        assertThat(size).isEqualTo(0);
    }

    @Test
    public void addsTournaments() throws Exception {
        // given
        TournamentRepository repository = emptyRepo();

        // when
        Set<String> tournaments = new HashSet<>(Arrays.asList("t1", "t2"));
        repository.replaceTournaments(tournaments);

        // then
        assertThat(repository.getTournementSize()).isEqualTo(2);
    }

    @Test
    public void getsTournaments() throws Exception {
        // given
        Set<String> expectedTournaments = new HashSet<>(Arrays.asList("t1", "t2"));
        TournamentRepository repository = repo(expectedTournaments, emptySet());

        // when
        Set<String> tournaments = repository.getTournaments();

        // then
        assertThat(tournaments).containsExactlyElementsOf(expectedTournaments);
    }
}