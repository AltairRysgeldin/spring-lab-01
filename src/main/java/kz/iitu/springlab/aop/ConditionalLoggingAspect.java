package kz.iitu.springlab.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
public class ConditionalLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(ConditionalLoggingAspect.class);

    @Value("${app.aspect.verbose:false}")
    private boolean verbose;

    @Around("execution(* kz.iitu.springlab.service.CatalogService.*(..))")
    public Object logConditionally(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().toShortString();
        long start = System.currentTimeMillis();

        try {
            Object result = pjp.proceed();
            long time = System.currentTimeMillis() - start;

            if (verbose) {
                log.info("[DEV] Подробно: Вызван {} | Аргументы: {} | Результат: {} | Время: {}ms",
                        methodName, Arrays.toString(pjp.getArgs()), result, time);
            } else {
                log.info("[PROD] Кратко: Выполнен {} за {}ms", methodName, time);
            }
            return result;

        } catch (Throwable ex) {
            long time = System.currentTimeMillis() - start;
            if (verbose) {
                log.error("[DEV] Ошибка в {} | Аргументы: {} | Исключение: {} | Время: {}ms",
                        methodName, Arrays.toString(pjp.getArgs()), ex.getMessage(), time);
            } else {
                log.error("[PROD] Ошибка: {} упал за {}ms", methodName, time);
            }
            throw ex;
        }
    }
}