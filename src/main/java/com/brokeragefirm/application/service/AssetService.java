package com.brokeragefirm.application.service;

import com.brokeragefirm.application.port.input.AssetUseCase;
import com.brokeragefirm.application.port.output.AssetPort;
import com.brokeragefirm.domain.model.Asset;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AssetService implements AssetUseCase {

  private final AssetPort assetPort;

  @Override
  public List<Asset> getCustomerAssets(UUID customerId) {
    log.info("Getting customer assets with customerId {}", customerId);
    return assetPort.getCustomerAssets(customerId);
  }
}
