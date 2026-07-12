package edu.OrbitaMarket.OrdersService;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("TASKING")
@Component
@NoArgsConstructor
@Setter
public class TaskingProduct extends Product{
    @Embedded
    private TimeWindow timeWindow;
    private String sensorType;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class TimeWindow {
        @Column(name = "from_date")
        private LocalDateTime from;

        @Column(name = "to_date")
        private LocalDateTime to;
    }

    @Override
    public String toString() {
        return super.toString() +
        "\t\ttime_window: " + timeWindow + '\n' +
        "\t\tsensor: " + sensorType + '\n' +
        "\t}\n" +
        '}';
    }
}
