package sejoharp;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class TournamentRepository {
    private Set<String> interestingTournaments;
    private Set<Notification> sentNotifications;

    private TournamentRepository() {
        this(new HashSet<>(), new HashSet<>());
    }

    public TournamentRepository(Set<String> interestingTournaments, Set<Notification> sentNotifications) {
        this.interestingTournaments = interestingTournaments;
        this.sentNotifications = sentNotifications;
    }

    public static TournamentRepository repo(Set<String> interestingTournaments, Set<Notification> sentNotifications) {
        return new TournamentRepository(interestingTournaments, sentNotifications);
    }

    public static TournamentRepository emptyRepo() {
        return new TournamentRepository(new HashSet<>(), new HashSet<>());
    }

    public int getSentNotificationSize() {
        return sentNotifications.size();
    }

    public void addNotification(Notification notification) {
        sentNotifications.add(notification);
    }

    public Set<Notification> getNotifications() {
        return sentNotifications;
    }

    public int getTournementSize() {
        return interestingTournaments.size();
    }

    public void replaceTournaments(Set<String> tournaments) {
        interestingTournaments = tournaments;
    }

    public void resetNotifications() {
        sentNotifications = new HashSet<>();
    }

    public Set<String> getTournaments() {
        return interestingTournaments;
    }

    public boolean isNewNotification(Notification notification) {
        return !sentNotifications.contains(notification);
    }
}
