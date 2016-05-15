package sejoharp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
public class DownloaderTest {
	@Autowired
	private Downloader downloader;
	
	@Test
	public void getsTournamentConfig() throws JsonParseException, JsonMappingException, IOException {
		TournamentConfig config = downloader.getTournamentConfig();

		assertThat(config, is(TestData.getTournament2PlayersConfig()));
	}
	@Test
	public void getsTournamentConfigWithSpecialCharacters() throws JsonParseException, JsonMappingException, IOException {
		TournamentConfig config = downloader.getTournamentConfig();

		assertThat(config.getPlayers().get(0).getName(), is("Userß"));
	}
}
