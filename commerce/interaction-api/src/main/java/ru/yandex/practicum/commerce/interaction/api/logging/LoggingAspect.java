package ru.yandex.practicum.commerce.interaction.api.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Around("@annotation(ru.yandex.practicum.commerce.interaction.api.logging)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Вызов метода: {}", joinPoint.getSignature());
        Object[] args = joinPoint.getArgs();
        log.info("Параметры метода: {}", args);

        Object result = joinPoint.proceed();

        log.info("Результат выполнения: {}", result);
        return result;
    }
}
