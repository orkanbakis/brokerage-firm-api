package com.brokeragefirm.adapter.web;

import com.brokeragefirm.application.port.input.AssetUseCase;
import com.brokeragefirm.domain.model.Asset;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/api/assets")
@Slf4j
@RequiredArgsConstructor
public class AssetController {

  private final AssetUseCase assetUseCase;

  @GetMapping("{customerId}")
  public ResponseEntity<List<Asset>> getCustomerAssets(@PathVariable("customerId") UUID customerId) {
    log.info("REST request to get all customer assets with customerId: {}", customerId);
    return ResponseEntity.ok(assetUseCase.getCustomerAssets(customerId));
  }
}
