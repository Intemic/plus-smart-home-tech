package ru.yandex.practicum.commerce.interaction.api.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
    String msgBefore() default "Вызов метода:";

    String msgAfter() default "Результат выполнения: ";
}
