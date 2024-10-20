package com.brokeragefirm.application.service;

import com.brokeragefirm.application.port.input.AdminUseCase;
import com.brokeragefirm.application.port.output.AssetPort;
import com.brokeragefirm.application.port.output.OrderPort;
import com.brokeragefirm.domain.enums.OrderSide;
import com.brokeragefirm.domain.enums.OrderStatus;
import com.brokeragefirm.domain.model.Asset;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor

public class AdminService implements AdminUseCase {

  private final AssetPort assetPort;
  private final OrderPort orderPort;

  @Override
  @Transactional
  public void matchOrders() {
    final int maxRetries = 3;
    int retryCount = 0;

    while (retryCount < maxRetries) {
      try {
        performOrderMatching();
        break;
      } catch (OptimisticLockException e) {
        retryCount++;
        if (retryCount >= maxRetries) {
          log.warn("Order matching failed after attempts {}", maxRetries);
          throw e;
        }
        log.info("Retrying order matching, attempt {}", retryCount);
      }
    }
  }

  private void performOrderMatching() {
    orderPort.getAllPendingOrders().forEach(order -> {
      order.setStatus(OrderStatus.MATCHED);
      orderPort.updateOrder(order);

      Asset stockAsset = assetPort.getCustomerStockAssetByCustomerIdAndAssetName(order.getCustomerId(),
          order.getAssetName());

      if (order.getOrderSide() == OrderSide.SELL) {
        Asset currencyAsset = assetPort.getCustomerCurrencyAsset(order.getCustomerId());
        currencyAsset.addUsableSize(order.getPrice().amount().multiply(BigDecimal.valueOf(order.getSize())));
        assetPort.updateAsset(currencyAsset);
      } else {
        stockAsset.addAssets(BigDecimal.valueOf(order.getSize()));
        assetPort.updateAsset(stockAsset);
      }
    });
  }
}
