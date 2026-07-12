package edu.OrbitaMarket.OrdersService;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final OutboxEventService outboxEventService;

    Product createProduct(Order order, String userId) throws InvalidPayloadException, InvalidPriceException, UnknownProductTypeException {
        Product product;
        switch (order.getProductType()) {
            case ProductType.ARCHIVE:
                product = new ArchiveProduct();
                if ((order.getPayload().getAoi() == null) | (order.getPayload().getCaptureDate() == null) | (order.getPayload().getSensorType()) == null) {
                    product.setStatus(Status.REJECTED);
                    product.setFailureReason("INVALID_PAYLOAD");
                    throw new InvalidPayloadException("Invalid payload for archive order");
                }
                if (order.getPrice() <= 0) {
                    product.setStatus(Status.REJECTED);
                    product.setFailureReason("INVALID_PRICE");
                    throw new InvalidPriceException("Price for order most be greater than zero");
                }
                ArchiveProduct archiveProduct = (ArchiveProduct)product; 
                archiveProduct.setCaptureDate(order.getPayload().getCaptureDate());
                archiveProduct.setSensorType(order.getPayload().getSensorType());
                break;
            case ProductType.MONITORING:
                product = new MonitoringProduct();
                if ((order.getPayload().getAoi() == null) | (order.getPayload().getCadence() == null) | (order.getPayload().getDurationDays()) == null) {
                    product.setStatus(Status.REJECTED);
                    product.setFailureReason("INVALID_PAYLOAD");
                    throw new InvalidPayloadException("Invalid payload for archive order");
                }
                if (order.getPrice() <= 0) {
                    product.setStatus(Status.REJECTED);
                    product.setFailureReason("INVALID_PRICE");
                    throw new InvalidPriceException("Price for order most be greater than zero");
                }
                MonitoringProduct monitoringProduct = (MonitoringProduct)product;
                monitoringProduct.setCadence(order.getPayload().getCadence());
                monitoringProduct.setDurationDays(order.getPayload().getDurationDays());
                break;
            case ProductType.TASKING:
                product = new TaskingProduct();
                if ((order.getPayload().getAoi() == null) | (order.getPayload().getTimeWindow() == null) | (order.getPayload().getSensorType()) == null) {
                    product.setStatus(Status.REJECTED);
                    product.setFailureReason("INVALID_PAYLOAD");
                    throw new InvalidPayloadException("Invalid payload for archive order");
                }
                if (order.getPrice() <= 0) {
                    product.setStatus(Status.REJECTED);
                    product.setFailureReason("INVALID_PRICE");
                    throw new InvalidPriceException("Price for order most be greater than zero");
                }
                TaskingProduct taskingProduct = (TaskingProduct)product;
                taskingProduct.setTimeWindow(order.getPayload().getTimeWindow());
                taskingProduct.setSensorType(order.getPayload().getSensorType());
                break;
            default:
                product = new Product();
                product.setStatus(Status.REJECTED);
                product.setFailureReason("UNKNOWN_PRODUCT_TYPE");
                throw new UnknownProductTypeException("Unknown product type: " + order.getProductType());
        }
        product.setUserId(userId);
        product.setAoi(order.getPayload().getAoi());
        product.setPrice(order.getPrice());
        product.setStatus(Status.PAYMENT_PENDING);
        product = productRepository.save(product);
        try {
            outboxEventService.publishToOutbox(new RequestedEvent(
                UUID.randomUUID(),
                product.getOrderId(),
                userId,
            product.getPrice()));
        }
        catch(RuntimeException e) {
            product.setStatus(Status.PAYMENT_FAILED);
            product.setFailureReason("FALSE_BILLING");
            product = productRepository.save(product);
        }
        return product;
    }

    List<Product> findProductsByUserId(String userId) {
        return productRepository.findAllByUserId(userId);
    }

    Product findProduct(Long orderId, String userId) throws OrderNotFoundException {
        Product product = productRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order with this order id is not found"));
        if (!product.getUserId().equals(userId)) {
            throw new OrderNotFoundException("You don't have permission access of this order");
        }
        return product;
    }
}
