package sejoharp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class App {

    public static void main(String[] args) throws Exception {
    	ApplicationContext app = SpringApplication.run(App.class);
        
    	Config config = app.getBean(Config.class);
    	System.out.println("loaded config: " + config);
    }
}