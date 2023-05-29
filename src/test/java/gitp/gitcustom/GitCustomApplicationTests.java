package gitp.gitcustom;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest(classes= GitCustomApplication.class)
class GitCustomApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void DateParseTest(){

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy ZZZZ");
		LocalDateTime dateTime = LocalDateTime.parse(dateOutput, formatter);

		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		int priorityRank = Integer.parseInt(dateTime.format(outputFormatter));
	}
}
