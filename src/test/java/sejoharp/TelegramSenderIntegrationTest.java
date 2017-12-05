package sejoharp;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static sejoharp.Config.emptyConfig;
import static sejoharp.Notification.notification;

public class TelegramSenderIntegrationTest {
    private static final String PATH = "/bot" + "123:mytoken";

    @Test
    public void createsValidMessage() throws Exception {
        // given
        MockWebServer server = new MockWebServer();
        server.start();
        HttpUrl url = server.url(PATH);

        Config config = emptyConfig().withTelegramUrl(url.toString());
        OkHttpClient httpClient = new OkHttpClient();
        TelegramSender sender = TelegramSender.sender(config, httpClient);

        // when
        Notification notification = notification(TestData.getMatch(), "1");
        sender.sendMessage(notification);

        // then
        RecordedRequest recordedRequest = server.takeRequest();
        assertThat(recordedRequest.getPath()).isEqualTo(PATH + "/sendMessage");
        assertThat(recordedRequest.getMethod()).isEqualTo("POST");
        assertThat(recordedRequest.getBody().readUtf8()).isEqualTo("chat_id=1&text=Tisch%3A2%20%7C%20Disziplin%3AGD%20Vorr.%20%7C%20Runde%3A2%20%7C%20User1%20%2F%20User2%20vs.%20User3%20%2F%20User4");

        server.shutdown();
    }

    @Test(enabled = false)
    public void sendsMessage() throws Exception {
        // given
        String token = "123:mytoken";
        Config config = emptyConfig().withTelegramUrl("https://api.telegram.org/bot" + token);
        OkHttpClient httpClient = new OkHttpClient();
        TelegramSender sender = TelegramSender.sender(config, httpClient);

        // when
        String chatId = "1";
        Notification notification = notification(TestData.getMatch(), chatId);
        sender.sendMessage(notification);
    }
}

