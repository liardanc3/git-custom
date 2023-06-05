package gitp.gitcustom.shell.aop.exception;

import java.io.PrintWriter;

public class ArgumentException extends RuntimeException {

    @Override
    public String toString(){
        return "The argument was not passed correctly. Please check and try again.";
    }

    @Override
    public void printStackTrace(PrintWriter s) {

    }
}
