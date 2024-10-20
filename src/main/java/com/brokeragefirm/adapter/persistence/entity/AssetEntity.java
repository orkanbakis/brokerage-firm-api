package com.brokeragefirm.adapter.persistence.entity;

import com.brokeragefirm.domain.enums.AssetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "assets")
@Getter
@Setter
public class AssetEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private UUID customerId;

  private String assetName;

  @Column(nullable = false)
  private BigDecimal size;

  @Column(nullable = false)
  private BigDecimal usableSize;

  @Enumerated(EnumType.STRING)
  private AssetType assetType;
}
