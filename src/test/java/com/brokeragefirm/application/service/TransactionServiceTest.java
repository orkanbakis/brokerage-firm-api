package com.brokeragefirm.application.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.brokeragefirm.adapter.persistence.TransactionRedisAdapter;
import com.brokeragefirm.adapter.web.dto.PreTransactionResponse;
import com.brokeragefirm.application.port.input.command.CreateTransactionCommand;
import com.brokeragefirm.application.port.output.AssetPort;
import com.brokeragefirm.common.constants.ErrorCode;
import com.brokeragefirm.domain.enums.Currency;
import com.brokeragefirm.domain.enums.TransactionType;
import com.brokeragefirm.domain.exceptions.TransactionException;
import com.brokeragefirm.domain.model.Asset;
import com.brokeragefirm.domain.model.Transaction;
import com.brokeragefirm.domain.valueobjects.Price;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class TransactionServiceTest {


  private TransactionRedisAdapter transactionRedisAdapter;
  private AssetPort assetPort;

  @InjectMocks
  private TransactionService transactionService;

  @BeforeEach
  void setUp() {
    transactionRedisAdapter = mock(TransactionRedisAdapter.class);
    assetPort = mock(AssetPort.class);
    transactionService = new TransactionService(transactionRedisAdapter, assetPort);

  }

  @Test
  void createPreTransaction_withValidWithdrawCommand_createsTransaction() {
    UUID customerId = UUID.randomUUID();
    CreateTransactionCommand command = new CreateTransactionCommand(TransactionType.WITHDRAW,
        new Price(BigDecimal.valueOf(1000), Currency.TRY),
        customerId);
    Asset asset = mock(Asset.class);
    when(assetPort.getCustomerCurrencyAsset(customerId)).thenReturn(asset);
    doNothing().when(asset).checkIfSufficientBalance(command.price().amount());
    PreTransactionResponse response = transactionService.createPreTransaction(command);
    assertNotNull(response.transactionId());
    verify(transactionRedisAdapter).saveTransaction(any(UUID.class), any(Transaction.class),
        eq(Duration.ofSeconds(15)));
  }

  @Test
  void createPreTransaction_withValidDepositCommand_createsTransaction() {
    UUID customerId = UUID.randomUUID();
    CreateTransactionCommand command = new CreateTransactionCommand(TransactionType.DEPOSIT,
        new Price(BigDecimal.valueOf(1000), Currency.TRY),
        customerId);
    PreTransactionResponse response = transactionService.createPreTransaction(command);
    assertNotNull(response.transactionId());
    verify(transactionRedisAdapter).saveTransaction(any(UUID.class), any(Transaction.class),
        eq(Duration.ofSeconds(15)));
  }

  @Test
  void completeTransaction_withValidTransaction_completesSuccessfully() {
    UUID transactionId = UUID.randomUUID();
    UUID customerId = UUID.randomUUID();
    Transaction transaction = new Transaction(BigDecimal.valueOf(1000), customerId, TransactionType.DEPOSIT);
    when(transactionRedisAdapter.getTransactionByTransactionId(transactionId)).thenReturn(transaction);
    Asset asset = mock(Asset.class);
    when(assetPort.getCustomerCurrencyAsset(customerId)).thenReturn(asset);
    transactionService.completeTransaction(transactionId);
    verify(asset).addAssets(transaction.amount());
    verify(assetPort).updateAsset(asset);
  }

  @Test
  void completeTransaction_withNonExistentTransaction_throwsException() {
    UUID transactionId = UUID.randomUUID();
    when(transactionRedisAdapter.getTransactionByTransactionId(transactionId)).thenReturn(null);
    assertThrows(TransactionException.class, () -> transactionService.completeTransaction(transactionId));
  }

  @Test
  void completeTransaction_withInsufficientBalance_throwsException() {
    UUID transactionId = UUID.randomUUID();
    UUID customerId = UUID.randomUUID();
    Transaction transaction = new Transaction(BigDecimal.valueOf(1000), customerId, TransactionType.WITHDRAW);
    when(transactionRedisAdapter.getTransactionByTransactionId(transactionId)).thenReturn(transaction);
    Asset asset = mock(Asset.class);
    when(assetPort.getCustomerCurrencyAsset(customerId)).thenReturn(asset);
    doThrow(new TransactionException(ErrorCode.INSUFFICIENT_BALANCE)).when(asset)
        .checkIfSufficientBalance(transaction.amount());
    assertThrows(TransactionException.class, () -> transactionService.completeTransaction(transactionId));
  }
}
