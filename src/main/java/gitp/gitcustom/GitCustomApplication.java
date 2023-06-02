package gitp.gitcustom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class GitCustomApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitCustomApplication.class, args);
	}

}