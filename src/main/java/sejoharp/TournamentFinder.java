package sejoharp;

import java.util.Set;

public interface TournamentFinder {
    Set<String> calculateInterestingTournaments(PlayerConfig config, Set<String> knownTournaments);
}
