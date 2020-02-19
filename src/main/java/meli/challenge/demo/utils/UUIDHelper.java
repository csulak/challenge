package meli.challenge.demo.utils;

import java.util.UUID;

public class UUIDHelper {

    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
