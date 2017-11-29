package sejoharp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class TournamentConfigReaderImpl implements TournamentConfigReader {
    private final Config config;

    @Autowired
    public TournamentConfigReaderImpl(Config config) {
        this.config = config;
    }

    @Override
    public TournamentConfig getTournamentConfig() throws JsonParseException, JsonMappingException, IOException {
        File configFile = new File(config.getTournamentConfigPath());
        return new ObjectMapper().readValue(configFile, TournamentConfig.class);
    }
}
