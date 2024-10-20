package com.brokeragefirm.adapter.web;

import com.brokeragefirm.application.port.input.AdminUseCase;
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
public class AdminController {

  private final AdminUseCase adminUseCase;

  @PostMapping("match-orders")
  public ResponseEntity<Void> matchOrders() {
    log.info("REST request to match orders");
    adminUseCase.matchOrders();
    return ResponseEntity.ok().build();
  }
}
