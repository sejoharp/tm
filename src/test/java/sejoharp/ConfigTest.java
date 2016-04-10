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
		assertThat(config.getSmtpserver(), is("smtp.web.de"));
		assertThat(config.getPort(), is("587"));
		assertThat(config.getUser(), is("username"));
		assertThat(config.getPassword(), is("password"));
		
		assertThat(config.getSenderaddress(), is("sender@domain.de"));
		assertThat(config.getTournamentUrl(), is("http://foobar.de"));
		assertThat(config.getSearchName1(), is("myname1"));
		assertThat(config.getRecipientaddress1(), is("recipient1@domain.de"));
		assertThat(config.getSearchName2(), is("myname2"));
		assertThat(config.getRecipientaddress2(), is("recipient2@domain.de"));
	}
}
