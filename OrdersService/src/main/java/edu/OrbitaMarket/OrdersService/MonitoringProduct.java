package edu.OrbitaMarket.OrdersService;

import org.springframework.stereotype.Component;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("MONITORING")
@Component
@NoArgsConstructor
public class MonitoringProduct extends Product{
    @Setter
    private Cadence cadence;
    @Setter
    private Long durationDays;

    public enum Cadence {
        DAILY,
        WEEKLY
    }

    @Override
    public String toString() {
        return super.toString() +
        "\t\tcadence: " + cadence + '\n' +
        "\t\tduration_days: " + durationDays + '\n' +
        "\t}\n" +
        "}";
    }
}
