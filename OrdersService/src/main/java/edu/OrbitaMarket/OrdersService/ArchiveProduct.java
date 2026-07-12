package edu.OrbitaMarket.OrdersService;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ARCHIVE")
@Component
@NoArgsConstructor
public class ArchiveProduct extends Product {
    @Setter
    private LocalDateTime captureDate;
    @Setter
    private String sensorType;

    @Override
    public String toString() {
        return super.toString() +
        "\t\tcapture_date: " + captureDate + '\n' +
        "\t\tsensor_type: " + sensorType + '\n' +
        "\t}\n" +
        "}";
    }
}
