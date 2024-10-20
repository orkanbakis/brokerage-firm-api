package com.brokeragefirm.adapter.persistence;

import com.brokeragefirm.adapter.persistence.repository.AssetRepository;
import com.brokeragefirm.application.mapper.AssetMapper;
import com.brokeragefirm.application.port.output.AssetPort;
import com.brokeragefirm.domain.enums.AssetType;
import com.brokeragefirm.domain.model.Asset;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AssetPostgresAdapter implements AssetPort {

  private final AssetMapper assetMapper;
  private final AssetRepository assetRepository;

  @Override
  public Asset getCustomerCurrencyAsset(UUID customerId) {
    log.info("Getting customer currency asset with customerId {}", customerId);
    Asset asset = assetMapper.toModel(assetRepository.findCustomerCurrencyBalance(customerId, AssetType.CURRENCY));
    asset.checkIfNull(asset);
    return asset;
  }

  @Override
  public Asset getCustomerStockAssetByCustomerIdAndAssetName(UUID customerId, String assetName) {
    log.info("Getting customer stock asset with customerId {} and assetName {}", customerId, assetName);
    Asset asset = assetMapper.toModel(
        assetRepository.findCustomerAssetByCustomerIdAndAssetName(customerId, assetName, AssetType.STOCK));
    if (asset == null) {
      asset = this.initializeAsset(customerId, assetName, AssetType.STOCK);
    }
    return asset;
  }

  @Override
  public void updateAsset(Asset asset) {
    log.info("Updating asset with assetId {}", asset.getId());
    assetRepository.save(assetMapper.toEntity(asset));
    log.info("Asset updated successfully with assetId {}", asset.getId());
  }

  @Override
  public List<Asset> getCustomerAssets(UUID customerId) {
    return assetRepository.findCustomerAssets(customerId).stream().map(assetMapper::toModel).toList();
  }

  private Asset initializeAsset(UUID customerId, String assetName, AssetType assetType) {
    return new Asset(customerId, assetName, BigDecimal.ZERO, BigDecimal.ZERO, assetType);
  }
}
