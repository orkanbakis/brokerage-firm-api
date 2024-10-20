package com.brokeragefirm.adapter.web;

import com.brokeragefirm.adapter.web.dto.PreTransactionResponse;
import com.brokeragefirm.application.port.input.TransactionUseCase;
import com.brokeragefirm.application.port.input.command.CreateTransactionCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("v1/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "Transaction API")
public class TransactionController {

  private final TransactionUseCase transactionUseCase;

  @Operation(summary = "Create a new pre-transaction")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Pre-transactions created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
      @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  @PostMapping("pre-transaction")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<PreTransactionResponse> createPreTransactions(@RequestBody CreateTransactionCommand command) {
    log.info("Received preTransaction request: {}", command);
    return ResponseEntity.ok(transactionUseCase.createPreTransaction(command));
  }


  @Operation(summary = "Complete transaction")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Transaction completed successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
      @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  @PutMapping("{transactionId}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Void> completeTransactions(@PathVariable("transactionId") UUID transactionId) {
    log.info("Received complete Transactions request: {}", transactionId);
    transactionUseCase.completeTransaction(transactionId);
    return ResponseEntity.ok().build();
  }
}
