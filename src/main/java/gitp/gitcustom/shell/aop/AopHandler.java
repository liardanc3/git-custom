package gitp.gitcustom.shell.aop;

import gitp.gitcustom.shell.aop.exception.ArgumentException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class AopHandler {

    /**
     * Exception Handlers
     */
    @AfterThrowing(value = "gitp.gitcustom.shell.aop.PointCuts.exception()", throwing = "e")
    public void gitAPIExceptionHandler(GitAPIException e){
        System.out.println("PointCuts.gitAPIExceptionHandler");
    }

    @AfterThrowing(value = "gitp.gitcustom.shell.aop.PointCuts.exception()", throwing = "e")
    public void noHeadExceptionHandler(NoHeadException e){
        System.out.println("ExceptionHandler.noHeadExceptionHandler");;
    }

    @AfterThrowing(value = "gitp.gitcustom.shell.aop.PointCuts.exception()", throwing = "e")
    public void noArgsExceptionHandler(ArgumentException e){
        System.out.println("AopHandler.noArgsExceptionHandler");
    }



}
