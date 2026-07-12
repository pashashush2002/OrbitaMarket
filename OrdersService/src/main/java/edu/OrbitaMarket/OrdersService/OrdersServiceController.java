package edu.OrbitaMarket.OrdersService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrdersServiceController {
    private final ProductService service;

    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(@RequestHeader String userId, @RequestBody Order order) {
        if (userId == null) {
            return ResponseEntity.status(400).body("Missing user_id");
        }
        try {
            Product product = service.createProduct(order, userId);
            return ResponseEntity.ok(product.toString());
        }
        catch (InvalidPayloadException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        catch (InvalidPriceException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        catch (UnknownProductTypeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<String> getOrders(@RequestHeader String userId) {
        if (userId == null) {
            return ResponseEntity.status(400).body("Missing user_id");
        }
        String body = "";
        for (Product product : service.findProductsByUserId(userId)) {
            body += product.toString() + '\n';
        } 
        return ResponseEntity.ok(body);
    }

    @GetMapping("/orders/{order_id}")
    public ResponseEntity<String> getOrder(@RequestHeader String userId, @PathVariable Long order_id) {
        if (userId == null) {
            return ResponseEntity.status(400).body("Missing user_id");
        }
        try {
            return ResponseEntity.ok(service.findProduct(order_id, userId).toString());
        }
        catch(OrderNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
     
}
