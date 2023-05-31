package gitp.gitcustom;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.shell.Shell;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
public class JUnitShellTest {

    @Autowired
    private Shell shell;

    @Test
    public void testShellCommand(CapturedOutput output) {
        shell.evaluate(() -> "test");
        String consoleOutput = output.getOut();
        System.out.println("consoleOutput = " + consoleOutput);
        String out = output.getOut();
        System.out.println("out = " + out);
        Assertions.assertThat(consoleOutput).contains("It's test text");
    }

}