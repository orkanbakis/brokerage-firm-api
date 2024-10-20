package com.brokeragefirm.application.service;

import com.brokeragefirm.adapter.web.dto.CancelOrderResponse;
import com.brokeragefirm.application.port.input.OrderPlacingUseCase;
import com.brokeragefirm.application.port.input.command.PlaceOrderCommand;
import com.brokeragefirm.application.port.output.AssetPort;
import com.brokeragefirm.application.port.output.OrderPort;
import com.brokeragefirm.application.port.output.OrderPublisherPort;
import com.brokeragefirm.common.constants.ErrorCode;
import com.brokeragefirm.domain.enums.OrderSide;
import com.brokeragefirm.domain.enums.OrderStatus;
import com.brokeragefirm.domain.exceptions.OrderException;
import com.brokeragefirm.domain.model.Asset;
import com.brokeragefirm.domain.model.Order;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderPlacingService implements OrderPlacingUseCase {

  private final OrderPublisherPort orderPublisherPort;
  private final OrderPort orderBookPort;
  private final AssetPort assetPort;

  @Override
  public void placeOrder(PlaceOrderCommand command) {
    log.info("Placing order with command {}", command);
    Order order = switch (command.side()) {
      case BUY -> placeBuyOrder(command);
      case SELL -> placeSellOrder(command);
    };

    orderPublisherPort.publishOrder(order);
    log.info("Order placed successfully with orderId {}", order.getId());
  }

  @Override
  @Transactional
  public CancelOrderResponse cancelOrder(UUID orderId) {
    log.info("Canceling order with orderId {}", orderId);
    Order order = orderBookPort.getOrderByOrderId(orderId);
    order.checkIfOrderExists(order);

    if (order.getStatus() != OrderStatus.PENDING) {
      log.warn("Order with orderId {} is not in PENDING status", orderId);
      throw new OrderException(ErrorCode.INVALID_ORDER_STATUS);
    }

    order.setStatus(OrderStatus.CANCELED);
    orderBookPort.updateOrder(order);

    if (order.getOrderSide() == OrderSide.BUY) {
      log.info("Refunding amount to customer with customerId {}", order.getCustomerId());
      Asset currencyAsset = assetPort.getCustomerCurrencyAsset(order.getCustomerId());
      currencyAsset.addAssets(order.getPrice().amount().multiply(BigDecimal.valueOf(order.getSize())));
      assetPort.updateAsset(currencyAsset);
      log.info("Amount refunded to customer with customerId {}", order.getCustomerId());
    } else {
      log.info("Refunding stock to customer with customerId {}", order.getCustomerId());
      Asset stockAsset = assetPort.getCustomerStockAssetByCustomerIdAndAssetName(order.getCustomerId(),
          order.getAssetName());
      stockAsset.addAssets(BigDecimal.valueOf(order.getSize()));
      assetPort.updateAsset(stockAsset);
      log.info("Stock refunded to customer with customerId {}", order.getCustomerId());
    }

    log.info("Order with orderId {} canceled successfully", orderId);
    return new CancelOrderResponse(orderId);
  }

  private Order placeBuyOrder(PlaceOrderCommand command) {
    log.info("Placing buy order with command {}", command);
    Asset asset = assetPort.getCustomerCurrencyAsset(command.customerId());
    asset.checkIfSufficientBalance(command.price().amount().multiply(BigDecimal.valueOf(command.size())));
    return new Order(command.customerId(), command.assetName(), command.side(), command.price(), command.size());
  }

  private Order placeSellOrder(PlaceOrderCommand command) {
    log.info("Placing sell order with command {}", command);
    Asset asset = assetPort.getCustomerStockAssetByCustomerIdAndAssetName(command.customerId(), command.assetName());
    asset.checkIfSufficientAsset(BigDecimal.valueOf(command.size()));
    return new Order(command.customerId(), command.assetName(), command.side(), command.price(), command.size());
  }
}
