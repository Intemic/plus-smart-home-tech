package ru.yandex.practicum.commerce.interaction.api.utill;

import java.util.UUID;

public class Convert {
    public static UUID converStringToUUID(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        try {
            return UUID.fromString(string);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
