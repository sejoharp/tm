package sejoharp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SpringBootTest
public class ConfigIntegrationTest extends AbstractTestNGSpringContextTests
{
    @Autowired
    Config config;

    @Test
    public void containsAllProperties() {
        assertThat(config.getSmtpserver(), is("smtp.domain.de"));
        assertThat(config.getPort(), is("587"));
        assertThat(config.getUser(), is("emailuser"));
        assertThat(config.getPassword(), is("secret"));
        assertThat(config.getSenderaddress(), is("sender@domain.de"));
        assertThat(config.getTournamentConfigPath(), is("src/test/resources/tournament-test.json"));
        assertThat(config.getTelegramUrl(), is("https://api.telegram.local/bot123:mytoken"));
    }
}
