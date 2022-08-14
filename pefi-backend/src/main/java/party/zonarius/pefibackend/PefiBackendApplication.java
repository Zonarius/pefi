package party.zonarius.pefibackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = { "classpath:application.properties", "classpath:secret.properties" })
public class PefiBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PefiBackendApplication.class, args);
	}

}
