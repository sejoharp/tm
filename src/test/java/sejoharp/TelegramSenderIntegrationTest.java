package sejoharp;

import okhttp3.*;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static sejoharp.Notification.notification;

public class TelegramSenderIntegrationTest {
    private static final String PATH = "/bot" + "123:mytoken";

    @Test
    public void sendsTelegramMessage() throws Exception {
        // given
        MockWebServer server = new MockWebServer();
        server.start();
        HttpUrl url = server.url(PATH);

        Config config = Config.emptyConfig().withTelegramUrl(url.toString());
        OkHttpClient httpClient = new OkHttpClient();
        TelegramSender sender = TelegramSender.sender(config, httpClient);

        // when
        Notification notification = notification(TestData.getMatch(), "ich@du.de", "1");
        sender.sendMessage(notification);

        // then
        RecordedRequest recordedRequest = server.takeRequest();
        assertThat(recordedRequest.getPath(), equalTo(PATH + "/sendMessage"));
        assertThat(recordedRequest.getMethod(), equalTo("POST"));
        assertThat(recordedRequest.getBody().readUtf8(), equalTo("chatId=1&text=Match%20%5Bteam1%3DUser1%20%2F%20User2%2C%20team2%3DUser3%20%2F%20User4%2C%20tableNumber%3D2%2C%20discipline%3DGD%20Vorr.%2C%20round%3D2%5D"));

        server.shutdown();
    }}
