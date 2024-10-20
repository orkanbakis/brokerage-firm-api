package com.brokeragefirm.application.port.output;

import com.brokeragefirm.domain.model.Asset;
import java.util.List;
import java.util.UUID;

public interface AssetPort {

  Asset getCustomerCurrencyAsset(UUID customerId);

  Asset getCustomerStockAssetByCustomerIdAndAssetName(UUID customerId, String assetName);

  void updateAsset(Asset asset);

  List<Asset> getCustomerAssets(UUID customerId);
}
