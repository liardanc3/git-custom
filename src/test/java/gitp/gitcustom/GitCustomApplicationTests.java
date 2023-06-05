package gitp.gitcustom;

import org.assertj.core.api.Assertions;
import org.eclipse.jgit.api.Git;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest(classes= GitCustomApplication.class)
class GitCustomApplicationTests {

    @Test
    void contextLoads(){

    }

    @Test
    void gitRemoteTest() throws IOException {
        Assertions.assertThat(Git.open(new File(".")))
                    .isInstanceOf(Git.class);
    }

}
