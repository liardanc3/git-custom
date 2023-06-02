package gitp.gitcustom.shell.aop;

import gitp.gitcustom.shell.aop.exception.NoArgsException;
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
     *
     */
    @AfterThrowing(value = "gitp.gitcustom.shell.aop.PointCuts.exception()", throwing = "e")
    public void gitAPIExceptionHandler(GitAPIException e){
        System.out.println("PointCuts.gitAPIExceptionHandler");
    }

    @AfterThrowing(value = "gitp.gitcustom.shell.aop.PointCuts.exception()", throwing = "e")
    public void noHeadExceptionHandler(NoHeadException e){
        System.out.println("ExceptionHandler.noHeadExceptionHandler");;
    }

    @AfterThrowing(value = "gitp.gitcustom.shell.aop.PointCuts.exception()", throwing = "o")
    public void noArgsExceptionHandler(NoArgsException o){
        System.out.println("asdasdasd");
    }

    //------------------------------------------------------------------------------------------------

    /**
     * RetryTask Handler
     *
     */

    @Around("gitp.gitcustom.shell.aop.PointCuts.retryTask()")
    public void retryTaskHandler(ProceedingJoinPoint joinPoint) throws Throwable{

        boolean retryFlag = true;

        while(retryFlag){
            try{
                retryFlag = false;
                joinPoint.proceed();
            } catch (Exception e){
                System.out.println("다시!!");
                retryFlag = true;
            }
        }
    }

}
