package com.messaging.rabbitmq.utils;

import java.time.Duration;
import java.util.function.BooleanSupplier;

public class TimeoutManagement {
    static boolean waitUntil(Duration timeout, BooleanSupplier condition) throws InterruptedException {
        int waited = 0;
        while (!condition.getAsBoolean() && waited < timeout.toMillis()) {
            Thread.sleep(100L);
            waited += 100;
        }
        return condition.getAsBoolean();
    }
}
