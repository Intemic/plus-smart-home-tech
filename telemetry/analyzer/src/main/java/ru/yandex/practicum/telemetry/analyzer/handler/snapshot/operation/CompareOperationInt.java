package ru.yandex.practicum.telemetry.analyzer.handler.snapshot.operation;

import org.springframework.stereotype.Component;

@Component
public enum CompareOperationInt implements CompareOperation<Integer, Integer> {
    EQUALS( ) {
        public boolean compare(Integer reference, Integer value) {
            if (reference == null || value == null )
                return false;

            return reference.equals(value);
        }
    },
    GREATER_THAN {
        public boolean compare(Integer reference, Integer value) {
            if (reference == null || value == null )
                return false;

            return reference > value;
        }
    },
    LOWER_THAN {
        public boolean compare(Integer reference, Integer value) {
            if (reference == null || value == null )
                return false;

            return reference < value;
        }
    };

//    public static OperationEnum valueOf(String name) {
//        return switch (name) {
//            case "EQUALS" -> EQUALS;
//            case "GREATER_THAN" -> GREATER_THAN;
//            case "LOWER_THAN" -> LOWER_THAN;
//            default -> throw new IllegalStateException("Недопустимая операция: " + name);
//        };
//    }
}
