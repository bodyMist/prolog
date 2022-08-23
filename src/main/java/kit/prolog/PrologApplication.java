package kit.prolog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching

@SpringBootApplication
public class PrologApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrologApplication.class, args);
	}

}
