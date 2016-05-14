package sejoharp;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class App {

	@Autowired
	private Config config;
	
    public static void main(String[] args) throws Exception {
    	ApplicationContext app = SpringApplication.run(App.class);
        
    	Config config = app.getBean(Config.class);
    	System.out.println("loaded config: " + config);
    }
    
    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setProtocol("SMTP");
        javaMailSender.setHost(config.getSmtpserver());
        javaMailSender.setPort(Integer.valueOf(config.getPort()));
        javaMailSender.setUsername(config.getUser());
        javaMailSender.setPassword(config.getPassword());
        Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty("mail.smtp.auth", "true");
        javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
		javaMailSender.setJavaMailProperties(javaMailProperties);
        return javaMailSender;
    }
}