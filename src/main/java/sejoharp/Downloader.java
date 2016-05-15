package sejoharp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Component
public class Downloader {
	public Document getPage(String url) {
		try {
			return Jsoup.connect(url).get();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public TournamentConfig getTournamentConfig(InputStream file)
			throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(file, TournamentConfig.class);
	}

}
