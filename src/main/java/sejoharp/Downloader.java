package sejoharp;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Downloader {
	private final Config config;

	@Autowired
	public Downloader(Config conig) {
		this.config = conig;
	}

	public Document getPage(String url) {
		try {
			return Jsoup.connect(url).get();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public TournamentConfig getTournamentConfig() throws JsonParseException, JsonMappingException, IOException {
		File configFile = new File(config.getTournamentConfigPath());
		return new ObjectMapper().readValue(configFile, TournamentConfig.class);
	}

}
