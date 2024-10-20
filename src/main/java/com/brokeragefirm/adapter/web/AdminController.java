package com.brokeragefirm.adapter.web;

import com.brokeragefirm.application.port.input.AdminUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/api/admin")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin API")
public class AdminController {

  private final AdminUseCase adminUseCase;

  @Operation(summary = "Matches all pending orders")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Orders matched"),
      @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
  @PostMapping("match-orders")
  public ResponseEntity<Void> matchOrders() {
    log.info("REST request to match orders");
    adminUseCase.matchOrders();
    return ResponseEntity.ok().build();
  }
}
