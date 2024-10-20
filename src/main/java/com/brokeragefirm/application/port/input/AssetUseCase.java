package com.brokeragefirm.application.port.input;

import com.brokeragefirm.domain.model.Asset;
import java.util.List;
import java.util.UUID;

public interface AssetUseCase {

  List<Asset> getCustomerAssets(UUID customerId);

}
