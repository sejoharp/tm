package sejoharp;

import java.util.Set;

public interface TournamentFinder {
    Set<String> calculateInterestingTournaments(TournamentConfig config, Set<String> knownTournaments);
}
