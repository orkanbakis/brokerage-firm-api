package com.brokeragefirm.adapter.persistence.repository;

import com.brokeragefirm.adapter.persistence.entity.AssetEntity;
import com.brokeragefirm.domain.enums.AssetType;
import com.brokeragefirm.domain.model.Asset;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssetRepository extends JpaRepository<AssetEntity, Long>, JpaSpecificationExecutor<Asset> {

  @Query("SELECT a FROM AssetEntity a WHERE a.customerId = :customerId AND a.assetType = :assetType")
  AssetEntity findCustomerCurrencyBalance(@Param("customerId") UUID customerId,
      @Param("assetType") AssetType assetType);

  @Query("SELECT a FROM AssetEntity a WHERE a.customerId = :customerId AND a.assetName = :assetName AND a.assetType = :assetType")
  AssetEntity findCustomerAssetByCustomerIdAndAssetName(@Param("customerId") UUID customerId,
      @Param("assetName") String assetName, @Param("assetType") AssetType assetType);

  @Query("SELECT a FROM AssetEntity a WHERE a.customerId = :customerId")
  List<AssetEntity> findCustomerAssets(UUID customerId);

}
