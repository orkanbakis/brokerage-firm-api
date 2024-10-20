package com.brokeragefirm.domain.model;


import com.brokeragefirm.common.constants.ErrorCode;
import com.brokeragefirm.domain.enums.AssetType;
import com.brokeragefirm.domain.exceptions.AssetException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Asset {

  private UUID id;

  @NotNull(message = "Customer ID cannot be null.")
  private UUID customerId;

  @NotNull(message = "Asset name cannot be null.")
  private String assetName;

  @Min(value = 0, message = "Total size must be 0 or greater.")
  private BigDecimal size;

  @Min(value = 0, message = "Usable size must be 0 or greater.")
  private BigDecimal usableSize;

  private AssetType assetType;

  public Asset(@NotNull UUID customerId, @NotNull String assetName, @Min(0) BigDecimal size,
      @Min(0) BigDecimal usableSize, AssetType assetType) {
    this.id = UUID.randomUUID();
    this.customerId = customerId;
    this.assetName = assetName;
    this.size = size;
    this.usableSize = usableSize;
    this.assetType = assetType;
  }

  public void checkIfNull(Asset asset) {
    if (asset == null) {
      throw new AssetException(ErrorCode.ASSET_NOT_FOUND);
    }
  }

  public void checkIfSufficientBalance(BigDecimal amount) {
    if (amount.compareTo(this.usableSize) > 0) {
      throw new AssetException(ErrorCode.INSUFFICIENT_BALANCE);
    }
  }

  public void checkIfSufficientAsset(BigDecimal amount) {
    if (amount.compareTo(this.usableSize) > 0) {
      throw new AssetException(ErrorCode.INSUFFIENCT_ASSET);
    }
  }

  public void addAssets(BigDecimal size) {
    if (size.compareTo(BigDecimal.ZERO) <= 0) {
      throw new AssetException(ErrorCode.ASSET_MUST_BE_ABOVE_ZERO);
    }
    this.size = this.size.add(size);
    this.usableSize = this.usableSize.add(size);
  }

  public void reduceAssets(BigDecimal size) {
    if (size.compareTo(BigDecimal.ZERO) <= 0) {
      throw new AssetException(ErrorCode.ASSET_MUST_BE_ABOVE_ZERO, "Size must be greater than 0 to reduce assets.");
    }
    if (usableSize.compareTo(size) < 0) {
      throw new AssetException(ErrorCode.ASSET_MUST_BE_ABOVE_ZERO,
          "Not enough usable assets to reduce by the given size.");
    }
    this.size = this.size.subtract(size);
    this.usableSize = this.usableSize.subtract(size);
  }

  public void reduceUsableSize(BigDecimal size) {
    if (size.compareTo(BigDecimal.ZERO) <= 0) {
      throw new AssetException(ErrorCode.ASSET_MUST_BE_ABOVE_ZERO, "Size must be greater than 0 to reduce assets.");
    }
    if (usableSize.compareTo(size) < 0) {
      throw new AssetException(ErrorCode.ASSET_MUST_BE_ABOVE_ZERO,
          "Not enough usable assets to reduce by the given size.");
    }

    this.usableSize = this.usableSize.subtract(size);
  }

  public void addUsableSize(BigDecimal size) {
    if (size.compareTo(BigDecimal.ZERO) <= 0) {
      throw new AssetException(ErrorCode.ASSET_MUST_BE_ABOVE_ZERO);
    }
    this.usableSize = this.usableSize.add(size);
  }
}
