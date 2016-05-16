package sejoharp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class DownloaderTest {

	@Test
	public void getsTournamentConfig() throws JsonParseException, JsonMappingException, IOException {
		Downloader downloader = new Downloader(TestData.getConig());
		TournamentConfig config = downloader.getTournamentConfig();

		assertThat(config, is(TestData.getTournament2PlayersConfig()));
	}

	@Test
	public void getsTournamentConfigWithSpecialCharacters()
			throws JsonParseException, JsonMappingException, IOException {
		Downloader downloader = new Downloader(TestData.getConig());
		TournamentConfig config = downloader.getTournamentConfig();

		assertThat(config.getPlayers().get(0).getName(), is("User1"));
	}
}
