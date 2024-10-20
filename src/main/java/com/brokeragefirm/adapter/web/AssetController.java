package com.brokeragefirm.adapter.web;

import com.brokeragefirm.application.port.input.AssetUseCase;
import com.brokeragefirm.domain.model.Asset;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Asset", description = "Asset API")
public class AssetController {

  private final AssetUseCase assetUseCase;

  @Operation(summary = "Lists all customer asssets")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Assets retrieved successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
      @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
  @GetMapping("{customerId}")
  public ResponseEntity<List<Asset>> getCustomerAssets(@PathVariable("customerId") UUID customerId) {
    log.info("REST request to get all customer assets with customerId: {}", customerId);
    return ResponseEntity.ok(assetUseCase.getCustomerAssets(customerId));
  }
}
