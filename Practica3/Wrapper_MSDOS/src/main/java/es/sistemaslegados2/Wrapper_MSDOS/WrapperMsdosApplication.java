package es.sistemaslegados2.Wrapper_MSDOS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class WrapperMsdosApplication {

	public static void main(String[] args) {
		// Para la correcta inicialización de java.awt.Robot
		System.setProperty("java.awt.headless", "false");
		SpringApplication.run(WrapperMsdosApplication.class, args);
	}

}
