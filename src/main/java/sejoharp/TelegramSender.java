package sejoharp;

import okhttp3.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TelegramSender {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TelegramSender.class);
    private static final String SEND_MESSAGE = "sendMessage";
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private final Config config;
    private final OkHttpClient httpClient;

    @Autowired
    private TelegramSender(Config config, OkHttpClient httpClient) {
        this.config = config;
        this.httpClient = httpClient;
    }

    public static TelegramSender sender(Config config, OkHttpClient httpClient) {
        return new TelegramSender(config, httpClient);
    }

    public void sendMessage(Notification notification) {
        HttpUrl url = HttpUrl.parse(config.getTelegramUrl())
                .newBuilder()
                .addPathSegment(SEND_MESSAGE)
                .build();
        RequestBody body = new FormBody.Builder()
                .add("chatId", notification.getChatId())
                .add("text", notification.getMatch().toString())
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            httpClient.newCall(request).execute();
        } catch (IOException e) {
            LOGGER.error("failed to send telegram notification. message: {}", e.getMessage());
        }
    }
}