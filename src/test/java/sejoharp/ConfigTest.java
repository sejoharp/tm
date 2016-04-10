package sejoharp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
public class ConfigTest {
	@Autowired
	Config config;

	@Test
	public void containsAllProperties() {
		assertThat(config.getSmtpserver(), is("smtp.domain.de"));
		assertThat(config.getPort(), is("587"));
		assertThat(config.getUser(), is("emailuser"));
		assertThat(config.getPassword(), is("secret"));
		
		assertThat(config.getSenderaddress(), is("sender@domain.de"));
		assertThat(config.getTournamentUrl(), is("http://domain.de"));
		assertThat(config.getSearchName1(), is("name1"));
		assertThat(config.getRecipientaddress1(), is("name1@domain.de"));
		assertThat(config.getSearchName2(), is("name2"));
		assertThat(config.getRecipientaddress2(), is("name2@domain.de"));
	}
}
