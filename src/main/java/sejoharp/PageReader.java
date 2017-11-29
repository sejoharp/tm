package sejoharp;

import org.jsoup.nodes.Document;

public interface PageReader {
    Document getPage(String url);
}
