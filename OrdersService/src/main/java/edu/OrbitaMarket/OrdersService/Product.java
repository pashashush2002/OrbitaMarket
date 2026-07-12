package edu.OrbitaMarket.OrdersService;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Table(name = "orders")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type", discriminatorType = DiscriminatorType.STRING)
@Component
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long orderId;
    @Setter
    @Column(name = "user_id")
    protected String userId;
    @Setter
    protected Long price;
    @Setter
    protected String aoi;
    @Setter
    protected Status status = Status.CREATED;
    @Setter
    @Column(name = "failture_reason")
    protected String failureReason;

    @Override
    public String toString() {
        return
        "{" +
        "\t{order_id: " + orderId + '\n' +
        "\tuser_id: " + userId + '\n' +
        "\tprice: " + price + '\n' +
        "\tstatus: " + status + '\n' +
        "\tpayload: \n" +
        "\t\taoi: " + aoi + '\n'; // продолжение в дочерних классах
    }
}
