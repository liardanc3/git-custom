package gitp.gitcustom.shell.aop.exception;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.util.Optional;

public class NoArgsException extends Exception {

    @Override
    public String toString(){
        return "NoArgsExceppppppp";
    }

    @Override
    public void printStackTrace(PrintWriter s) {

    }
}
