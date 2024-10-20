package com.brokeragefirm.application.port.input;

import com.brokeragefirm.adapter.web.dto.CancelOrderResponse;
import com.brokeragefirm.application.port.input.command.PlaceOrderCommand;
import java.util.UUID;

public interface OrderPlacingUseCase {

  void placeOrder(PlaceOrderCommand command);

  CancelOrderResponse cancelOrder(UUID orderId);
}
