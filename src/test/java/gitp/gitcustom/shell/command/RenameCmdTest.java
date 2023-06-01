package gitp.gitcustom.shell.command;

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
class RenameCmdTest {

    @Autowired
    private Shell shell;

    @Test
    void test(CapturedOutput output) {
        shell.evaluate(() -> "rename --file Spring winter");
        String consoleOutput = output.getOut();
        System.out.println("consoleOutput = " + consoleOutput);
        String out = output.getOut();
        System.out.println("out = " + out);
        Assertions.assertThat(consoleOutput).contains("rename test");
    }
}