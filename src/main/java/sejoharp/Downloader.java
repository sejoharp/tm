package sejoharp;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Downloader {
	@Autowired
    private ResourceLoader resourceLoader;
	
	public Document getPage(String url) {
		try {
			return Jsoup.connect(url).get();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public TournamentConfig getTournamentConfig()
			throws JsonParseException, JsonMappingException, IOException {
		InputStream file = resourceLoader.getResource("classpath:tournament.json").getInputStream();
		return new ObjectMapper().readValue(file, TournamentConfig.class);
	}

}
