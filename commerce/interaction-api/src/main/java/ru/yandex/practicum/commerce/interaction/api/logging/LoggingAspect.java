package ru.yandex.practicum.commerce.interaction.api.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    private final int value = 0;

    @Around("@annotation(ru.yandex.practicum.commerce.interaction.api.logging.Loggable)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Loggable loggableAnnotation =  method.getAnnotation(Loggable.class);

        log.info(loggableAnnotation.msgBefore() + " {}", joinPoint.getSignature());
        Object[] args = joinPoint.getArgs();
        log.info("Параметры метода: {}", args);

        Object result = joinPoint.proceed();

        log.info(loggableAnnotation.msgAfter() + " {}", result);
        return result;
    }
}
