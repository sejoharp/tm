package sejoharp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static sejoharp.Player.createPlayer;
import static sejoharp.TournamentRepository.emptyRepo;

public class NotifyPlayerJobIntegrationTest {
    private NotifyPlayerJob notifyPlayerJob;
    private TournamentParserImpl parser;
    private Document doc;

    @BeforeTest
    public void setup() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("tournament2.html").getFile());
        doc = Jsoup.parse(file, "utf-8", "");

        parser = new TournamentParserImpl();
        PageReader pageReader = url -> null;
        notifyPlayerJob = NotifyPlayerJob.newNotifyPlayerJob(null, parser, pageReader, null, emptyRepo());
    }

    @Test
    public void findsMatchesToNotify() throws IOException {
        // given
        List<Match> matches = parser.getMatchesFrom(doc);

        // when
        List<Notification> newMatches = notifyPlayerJob.findNotificationsForPlayer(matches, createPlayer("Reimer", "1"))
                .collect(toList());

        // then
        assertThat(newMatches.get(0).getMatch().getTeam1()).contains("Reimer");
        assertThat(newMatches.get(0).getChatId()).isEqualTo("1");
    }

    @Test
    public void finds2MatchesToNotify() throws IOException {
        // given
        List<Match> matches = parser.getMatchesFrom(doc);
        List<Player> players = Arrays.asList(
                createPlayer("Reimer", "1"),
                createPlayer("Borck", "2"));

        // when
        List<Notification> newMatches = notifyPlayerJob.findAllNotifications(matches, players).collect(toList());

        // then
        assertThat(newMatches.get(0).getMatch().getTeam1()).contains("Reimer");
        assertThat(newMatches.get(0).getChatId()).isEqualTo("1");
        assertThat(newMatches.get(1).getMatch().getTeam1()).contains("Borck");
        assertThat(newMatches.get(1).getChatId()).isEqualTo("2");
        assertThat(newMatches).hasSize(2);
    }
}
