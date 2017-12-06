package sejoharp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerConfigReaderTest {

    @Test
    public void getsPlayerConfig() throws JsonParseException, JsonMappingException, IOException {
        // given
        PlayerConfigReader playerConfigReader = new PlayerConfigFileReader(TestData.getConfig());

        // when
        PlayerConfig config = playerConfigReader.getPlayerConfig();

        // then
        assertThat(config).isEqualTo(TestData.get2PlayersConfig2());
    }
}
