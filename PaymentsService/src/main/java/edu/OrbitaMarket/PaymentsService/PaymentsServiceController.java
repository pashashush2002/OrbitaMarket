package edu.OrbitaMarket.PaymentsService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentsServiceController {
    private final AccountService service;

    @PostMapping("/accounts")
    public ResponseEntity<String> createAccount(@RequestHeader String userId) {
        try {
            service.addAccount(userId);
            return ResponseEntity.ok().build();
        }
        catch (AccountNotFoundException e) {
            return ResponseEntity.status(409).body("error_code: ACCOUNT_ALREADY_EXISTS,\n" +
                    "message: " + e.getMessage() + '\n' +
                    "timestamp: " + LocalDateTime.now());
        }
    }

    @PostMapping("/accounts/top-up")
    public ResponseEntity<String> topUpAccount(@RequestHeader String userId, @RequestBody Long amount) {
        try {
            service.topUp(userId, amount);
            return ResponseEntity.ok().build();
        }
        catch (AccountNotFoundException e) {
            return ResponseEntity.status(404).body("error_code: ACCOUNT_NOT_FOUND\n" +
                    "message: " + e.getMessage() + '\n' +
                    "timestamp: " + LocalDateTime.now());
        }
        catch (InvalidAmountException iae) {
            return ResponseEntity.status(400).body("error_code: INVALID_AMOUNT\n" +
                    "message: " + iae.getMessage() + '\n' +
                    "timestamp: " + LocalDateTime.now());
        }
    }

    @GetMapping("/accounts/balance")
    public ResponseEntity<String> getBalance(@RequestHeader String userId) {
        try {
            return ResponseEntity.ok("user-id: " + userId + '\n' +
                    "balance: " + service.getBalance(userId) + '\n' +
                    "currency: geocredits");
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(404).body("error_code: ACCOUNT_NOT_FOUND\n" +
                    "message: " + e.getMessage() + '\n' +
                    "timestamp: " + LocalDateTime.now());
        }
    }
}
