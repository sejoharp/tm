package sejoharp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class TournamentConfigReaderTest {

    @Test
    public void getsTournamentConfig() throws JsonParseException, JsonMappingException, IOException {
        // given
        TournamentConfigReader tournamentConfigReader = new TournamentConfigFileReader(TestData.getConfig());

        // when
        TournamentConfig config = tournamentConfigReader.getTournamentConfig();

        // then
        assertThat(config).isEqualTo(TestData.getTournament2PlayersConfig2());
    }
}
