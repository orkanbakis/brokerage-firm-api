package com.brokeragefirm.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.brokeragefirm.adapter.web.dto.CancelOrderResponse;
import com.brokeragefirm.application.port.input.command.PlaceOrderCommand;
import com.brokeragefirm.application.port.output.AssetPort;
import com.brokeragefirm.application.port.output.OrderPort;
import com.brokeragefirm.application.port.output.OrderPublisherPort;
import com.brokeragefirm.common.constants.ErrorCode;
import com.brokeragefirm.domain.enums.Currency;
import com.brokeragefirm.domain.enums.OrderSide;
import com.brokeragefirm.domain.enums.OrderStatus;
import com.brokeragefirm.domain.exceptions.OrderException;
import com.brokeragefirm.domain.model.Asset;
import com.brokeragefirm.domain.model.Order;
import com.brokeragefirm.domain.valueobjects.Price;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class OrderPlacingServiceTest {

  private OrderPublisherPort orderPublisherPort;
  private OrderPort orderPort;
  private AssetPort assetPort;

  @InjectMocks
  private OrderPlacingService orderPlacingService;

  @BeforeEach
  void setUp() {
    orderPublisherPort = mock(OrderPublisherPort.class);
    orderPort = mock(OrderPort.class);
    assetPort = mock(AssetPort.class);
    orderPlacingService = new OrderPlacingService(orderPublisherPort, orderPort, assetPort);
  }

  @Test
  void placeOrder_withValidBuyOrder_publishesOrder() {
    PlaceOrderCommand command = new PlaceOrderCommand(UUID.randomUUID(), "AAPL", OrderSide.BUY,
        new Price(BigDecimal.valueOf(1500), Currency.TRY), 10);
    Asset asset = mock(Asset.class);
    when(assetPort.getCustomerCurrencyAsset(command.customerId())).thenReturn(asset);
    doNothing().when(asset).checkIfSufficientBalance(any(BigDecimal.class));
    orderPlacingService.placeOrder(command);
    verify(orderPublisherPort).publishOrder(any(Order.class));
  }

  @Test
  void placeOrder_withValidSellOrder_publishesOrder() {
    PlaceOrderCommand command = new PlaceOrderCommand(UUID.randomUUID(), "AAPL", OrderSide.SELL,
        new Price(BigDecimal.valueOf(1500), Currency.TRY), 10);
    Asset asset = mock(Asset.class);
    when(assetPort.getCustomerStockAssetByCustomerIdAndAssetName(command.customerId(), command.assetName())).thenReturn(
        asset);
    doNothing().when(asset).checkIfSufficientBalance(any(BigDecimal.class));
    orderPlacingService.placeOrder(command);
    verify(orderPublisherPort).publishOrder(any(Order.class));
  }

  @Test
  void cancelOrder_withPendingBuyOrder_refundsAmount() {
    UUID orderId = UUID.randomUUID();
    UUID customerId = UUID.randomUUID();
    Order order = new Order(customerId, "AAPL", OrderSide.BUY, new Price(BigDecimal.valueOf(1500), Currency.TRY), 10);
    when(orderPort.getOrderByOrderId(orderId)).thenReturn(order);
    Asset asset = mock(Asset.class);
    when(assetPort.getCustomerCurrencyAsset(customerId)).thenReturn(asset);

    CancelOrderResponse response = orderPlacingService.cancelOrder(orderId);

    assertEquals(orderId, response.orderId());
    verify(orderPort).updateOrder(order);
    verify(asset).addAssets(order.getPrice().amount().multiply(BigDecimal.valueOf(order.getSize())));
    verify(assetPort).updateAsset(asset);
  }

  @Test
  void cancelOrder_withPendingSellOrder_refundsStock() {
    UUID orderId = UUID.randomUUID();
    UUID customerId = UUID.randomUUID();
    Order order = new Order(customerId, "AAPL", OrderSide.SELL, new Price(BigDecimal.valueOf(1500), Currency.TRY), 10);
    when(orderPort.getOrderByOrderId(orderId)).thenReturn(order);
    Asset stockAsset = mock(Asset.class);
    when(assetPort.getCustomerStockAssetByCustomerIdAndAssetName(customerId, "AAPL")).thenReturn(stockAsset);

    CancelOrderResponse response = orderPlacingService.cancelOrder(orderId);

    assertEquals(orderId, response.orderId());
    verify(orderPort).updateOrder(order);
    verify(stockAsset).addAssets(BigDecimal.valueOf(order.getSize()));
    verify(assetPort).updateAsset(stockAsset);
  }

  @Test
  void cancelOrder_withNonPendingOrder_throwsException() {
    UUID orderId = UUID.randomUUID();
    Order order = mock(Order.class);
    when(orderPort.getOrderByOrderId(orderId)).thenReturn(order);
    when(order.getStatus()).thenReturn(OrderStatus.CANCELED);

    assertThrows(OrderException.class, () -> orderPlacingService.cancelOrder(orderId));
  }

  @Test
  void cancelOrder_withNonExistentOrder_throwsException() {
    UUID orderId = UUID.randomUUID();
    when(orderPort.getOrderByOrderId(orderId)).thenThrow(new OrderException(ErrorCode.ORDER_NOT_FOUND));

    OrderException orderException = assertThrows(OrderException.class, () -> orderPlacingService.cancelOrder(orderId));
    assertEquals(ErrorCode.ORDER_NOT_FOUND.getCode(), orderException.getErrorCode());
  }

  @Test
  void cancelOrder_withPendingSellOrder_doesNotRefundAmount() {
    UUID orderId = UUID.randomUUID();
    UUID customerId = UUID.randomUUID();
    Order order = new Order(customerId, "AAPL", OrderSide.SELL, new Price(BigDecimal.valueOf(1500), Currency.TRY), 10);
    when(orderPort.getOrderByOrderId(orderId)).thenReturn(order);
    when(assetPort.getCustomerStockAssetByCustomerIdAndAssetName(customerId, "AAPL")).thenReturn(mock(Asset.class));

    CancelOrderResponse response = orderPlacingService.cancelOrder(orderId);

    assertEquals(orderId, response.orderId());
    verify(orderPort).updateOrder(order);
    verify(assetPort, never()).getCustomerCurrencyAsset(customerId);
  }
}
