package com.brokeragefirm.application.port.input.command;

import com.brokeragefirm.domain.enums.OrderSide;
import com.brokeragefirm.domain.valueobjects.Price;
import java.util.UUID;


public record PlaceOrderCommand(UUID customerId, String assetName, OrderSide side, Price price, int size) {

}
