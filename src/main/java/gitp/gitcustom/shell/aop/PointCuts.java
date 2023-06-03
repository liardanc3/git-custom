package gitp.gitcustom.shell.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PointCuts {

    @Pointcut("@annotation(gitp.gitcustom.shell.aop.annotation.ExceptionAspect)")
    public void exception(){};

}
