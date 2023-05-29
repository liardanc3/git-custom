package gitp.gitcustom;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest(classes= GitCustomApplication.class)
class GitCustomApplicationTests {

	@Test
	void DateParseTest() throws ParseException {
		String input = "2023-05-30 06:40:05 +0900";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss Z");

		Date date = formatter.parse(input);

		long priorityRank = date.getTime();
		System.out.println("priorityRank = " + priorityRank);

	}
}
