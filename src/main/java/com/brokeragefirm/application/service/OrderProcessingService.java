package com.brokeragefirm.application.service;

import com.brokeragefirm.application.port.input.OrderProcessingUseCase;
import com.brokeragefirm.application.port.output.AssetPort;
import com.brokeragefirm.application.port.output.OrderPort;
import com.brokeragefirm.common.constants.ErrorCode;
import com.brokeragefirm.domain.enums.OrderSide;
import com.brokeragefirm.domain.exceptions.OrderException;
import com.brokeragefirm.domain.model.Asset;
import com.brokeragefirm.domain.model.Order;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProcessingService implements OrderProcessingUseCase {

  private final OrderPort orderPort;
  private final AssetPort assetPort;

  @Override
  public void processOrder(Order order) {
    if (order == null) {
      throw new OrderException(ErrorCode.NO_ORDER_FOUND_TO_PROCESS);
    }

    if (order.getOrderSide() == OrderSide.BUY) {
      Asset currencyAsset = assetPort.getCustomerCurrencyAsset(order.getCustomerId());
      currencyAsset.reduceUsableSize(order.getPrice().amount().multiply(BigDecimal.valueOf(order.getSize())));
      assetPort.updateAsset(currencyAsset);
    } else {
      Asset stockAsset = assetPort.getCustomerStockAssetByCustomerIdAndAssetName(order.getCustomerId(),
          order.getAssetName());
      stockAsset.reduceUsableSize(BigDecimal.valueOf(order.getSize()));
      assetPort.updateAsset(stockAsset);
    }

    orderPort.addOrder(order);
  }
}
