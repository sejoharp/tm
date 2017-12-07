package sejoharp;

import okhttp3.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TelegramSenderImpl implements TelegramSender {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TelegramSenderImpl.class);
    private static final String SEND_MESSAGE = "sendMessage";
    private final Config config;
    private final OkHttpClient httpClient;

    @Autowired
    private TelegramSenderImpl(Config config, OkHttpClient httpClient) {
        this.config = config;
        this.httpClient = httpClient;
    }

    public static TelegramSender sender(Config config, OkHttpClient httpClient) {
        return new TelegramSenderImpl(config, httpClient);
    }

    @Override
    public void sendMessage(Notification notification) {
        HttpUrl url = composeRequestUrl();
        RequestBody body = composeRequestBody(notification);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            logError(response);
            response.close();
        } catch (IOException e) {
            LOGGER.error("failed to send telegram notification. message: {}", e.getMessage());
        }
    }

    private FormBody composeRequestBody(Notification notification) {
        return new FormBody.Builder()
                .add("chat_id", notification.getChatId())
                .add("text", notification.getMatch().toFormattedString())
                .build();
    }

    private HttpUrl composeRequestUrl() {
        return HttpUrl.parse(config.getTelegramUrl())
                .newBuilder()
                .addPathSegment(SEND_MESSAGE)
                .build();
    }

    private static void logError(Response response) throws IOException {
        if (response.code() != 200){
            LOGGER.error(response.toString());
            LOGGER.error(response.body().string());
        }
    }
}
