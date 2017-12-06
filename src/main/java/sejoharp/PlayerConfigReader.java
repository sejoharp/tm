package sejoharp;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

public interface PlayerConfigReader {
    PlayerConfig getPlayerConfig() throws JsonParseException, JsonMappingException, IOException;
}
