package gitp.gitcustom.command;

import gitp.gitcustom.command.function.Rename;
import org.springframework.shell.standard.ShellComponent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.PriorityQueue;

@ShellComponent
public interface CustomCommands {

    // mainTest
    void test();

}
