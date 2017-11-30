package sejoharp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TournamentConfigReaderTest {

    @Test
    public void getsTournamentConfig() throws JsonParseException, JsonMappingException, IOException {
        // given
        TournamentConfigReader tournamentConfigReader = new TournamentConfigFileReader(TestData.getConfig());

        // when
        TournamentConfig config = tournamentConfigReader.getTournamentConfig();

        // then
        assertThat(config, is(TestData.getTournament2PlayersConfig2()));
    }
}
