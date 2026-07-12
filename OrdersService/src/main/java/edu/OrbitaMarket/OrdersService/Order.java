package edu.OrbitaMarket.OrdersService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Order {
    @Getter
    private final Long price;
    @Getter
    private final ProductType productType;
    @Getter
    private final Payload payload;
}
