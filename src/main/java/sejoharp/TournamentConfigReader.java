package sejoharp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

public interface TournamentConfigReader {
    TournamentConfig getTournamentConfig() throws JsonParseException, JsonMappingException, IOException;
}
