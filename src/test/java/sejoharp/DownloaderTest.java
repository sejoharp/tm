package sejoharp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class DownloaderTest {
	@Test
	public void getsTournamentConfig() throws JsonParseException, JsonMappingException, IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("tournament.json").getFile());
		TournamentConfig config = new Downloader().getTournamentConfig(file);

		assertThat(config, is(TestData.getTournament2PlayersConfig()));
	}
	@Test
	public void getsTournamentConfigWithSpecialCharacters() throws JsonParseException, JsonMappingException, IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("tournament-test.json").getFile());
		TournamentConfig config = new Downloader().getTournamentConfig(file);

		assertThat(config.getPlayers().get(0).getName(), is("Userß"));
	}
}
