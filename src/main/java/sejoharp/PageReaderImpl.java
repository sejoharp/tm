package sejoharp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PageReaderImpl implements PageReader {

    public static PageReaderImpl pageReader() {
        return new PageReaderImpl();
    }

    private PageReaderImpl() {
    }

    @Override
    public Document getPage(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
