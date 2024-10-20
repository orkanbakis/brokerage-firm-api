package com.brokeragefirm.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.brokeragefirm.application.port.output.AssetPort;
import com.brokeragefirm.application.port.output.OrderPort;
import com.brokeragefirm.common.constants.ErrorCode;
import com.brokeragefirm.domain.enums.Currency;
import com.brokeragefirm.domain.enums.OrderSide;
import com.brokeragefirm.domain.exceptions.OrderException;
import com.brokeragefirm.domain.model.Asset;
import com.brokeragefirm.domain.model.Order;
import com.brokeragefirm.domain.valueobjects.Price;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class OrderProcessingServiceTest {


  private OrderPort orderPort;
  private AssetPort assetPort;

  @InjectMocks
  private OrderProcessingService orderProcessingService;

  @BeforeEach
  void setUp() {
    orderPort = mock(OrderPort.class);
    assetPort = mock(AssetPort.class);
    orderProcessingService = new OrderProcessingService(orderPort, assetPort);
  }

  @Test
  void processOrder_withBuyOrder_reducesAssets() {
    UUID customerId = UUID.randomUUID();
    Order order = new Order(customerId, "AAPL", OrderSide.BUY, new Price(BigDecimal.valueOf(1500), Currency.TRY), 10);
    Asset asset = mock(Asset.class);
    when(assetPort.getCustomerCurrencyAsset(customerId)).thenReturn(asset);

    orderProcessingService.processOrder(order);

    verify(asset).reduceAssets(order.getPrice().amount().multiply(BigDecimal.valueOf(order.getSize())));
    verify(assetPort).updateAsset(asset);
    verify(orderPort).addOrder(order);
  }

  @Test
  void processOrder_withSellOrder_doesNotReduceAssets() {
    UUID customerId = UUID.randomUUID();
    Order order = new Order(customerId, "AAPL", OrderSide.SELL, new Price(BigDecimal.valueOf(1500), Currency.TRY), 10);
    when(assetPort.getCustomerStockAssetByCustomerIdAndAssetName(customerId, order.getAssetName())).thenReturn(mock(Asset.class));
    orderProcessingService.processOrder(order);
    verify(assetPort, never()).getCustomerCurrencyAsset(customerId);
    verify(orderPort).addOrder(order);
  }

  @Test
  void processOrder_withNullOrder_throwsException() {
    OrderException orderException = assertThrows(OrderException.class, () -> orderProcessingService.processOrder(null));
    assertEquals(ErrorCode.NO_ORDER_FOUND_TO_PROCESS.getCode(), orderException.getErrorCode());
  }
}
