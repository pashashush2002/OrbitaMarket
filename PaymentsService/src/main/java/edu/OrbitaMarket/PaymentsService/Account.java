package edu.OrbitaMarket.PaymentsService;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
public class Account {
    @Id
    @Setter
    @Getter
    private String x_user_id;

    @Setter
    @Getter
    @Column
    private Long balance;

    @Setter
    @Getter
    @Column
    @Version
    private Long version;
}
