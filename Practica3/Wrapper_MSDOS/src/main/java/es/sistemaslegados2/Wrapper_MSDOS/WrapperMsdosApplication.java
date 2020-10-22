package es.sistemaslegados2.Wrapper_MSDOS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
@RestController
public class WrapperMsdosApplication {

	public static void main(String[] args) {
		SpringApplication.run(WrapperMsdosApplication.class, args);
	}

	@GetMapping(path = "/prueba")
//	@ResponseBody
	public String prueba(@RequestParam(name = "name", defaultValue = "mundo") String name) {
		try {
			Process p = Runtime.getRuntime().exec("echo Hello " + name);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			String lineret = null;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
				lineret = line;
			}
			in.close();
			return lineret;
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		}
	}
}
