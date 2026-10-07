package kz.iitu.springlab.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* kz.iitu.springlab.service.CatalogService.*(..))")
    public void logBeforeMethodCall(JoinPoint joinPoint) {
        System.out.println("====== AOP ЛОГ ======");
        System.out.println("Вызван метод: " + joinPoint.getSignature().getName());
        System.out.println("=====================");
    }
}