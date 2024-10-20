package com.brokeragefirm.application.port.input;

import com.brokeragefirm.domain.model.Order;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface OrderListingUseCase {

  List<Order> getAllOrdersByCustomerId(UUID customerId, Pageable pageable);

}
