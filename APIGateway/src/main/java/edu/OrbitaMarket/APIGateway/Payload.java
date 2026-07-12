package edu.OrbitaMarket.APIGateway;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Payload {
    private final String aoi; // принимаем как есть, обработка в других модулях
    private final LocalDateTime captureDate;
    private final String sensorType;
    private final Cadence cadence;
    private final Long durationDays;
    private final TimeWindow timeWindow;
    public enum Cadence {
        DAILY,
        WEEKLY
    }
    @RequiredArgsConstructor
    @Getter
    public static class TimeWindow {
        private final LocalDateTime from;

        private final LocalDateTime to;
    }
}
