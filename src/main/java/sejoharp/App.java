package sejoharp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class App {
    private static Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        ApplicationContext app = SpringApplication.run(App.class);
        log.info("loaded config: " + app.getBean(Config.class));
    }
}
