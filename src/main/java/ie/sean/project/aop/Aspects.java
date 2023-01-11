package ie.sean.project.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Slf4j
@Component
public class Aspects {

    @Before("within(@org.springframework.web.bind.annotation.RestController *)")
    public void beforeRestControllerMethods(JoinPoint joinPoint) throws Throwable {
        log.error("Signature and Argument/s - " + joinPoint.getSignature().getName() + ":" + Arrays.toString(joinPoint.getArgs()));
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void springPointcut() {}

    // Will spit out the class and error if one occurs
    @AfterThrowing(pointcut = "springPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
    }



}
