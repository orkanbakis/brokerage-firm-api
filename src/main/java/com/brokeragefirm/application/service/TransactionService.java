package com.brokeragefirm.application.service;

import com.brokeragefirm.adapter.persistence.TransactionRedisAdapter;
import com.brokeragefirm.adapter.web.dto.PreTransactionResponse;
import com.brokeragefirm.application.port.input.TransactionUseCase;
import com.brokeragefirm.application.port.input.command.CreateTransactionCommand;
import com.brokeragefirm.application.port.output.AssetPort;
import com.brokeragefirm.common.constants.ErrorCode;
import com.brokeragefirm.domain.enums.TransactionType;
import com.brokeragefirm.domain.exceptions.TransactionException;
import com.brokeragefirm.domain.model.Asset;
import com.brokeragefirm.domain.model.Transaction;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionService implements TransactionUseCase {

  private final TransactionRedisAdapter redisAdapter;
  private final AssetPort assetPort;

  @Override
  public PreTransactionResponse createPreTransaction(CreateTransactionCommand command) {
    log.info("Creating pre transaction with command {}", command);
    Asset asset = assetPort.getCustomerCurrencyAsset(command.customerId());
    if (command.transactionType().equals(TransactionType.WITHDRAW)) {
      asset.checkIfSufficientBalance(command.price().amount());
    }

    UUID transactionId = UUID.randomUUID();
    Transaction transaction = new Transaction(command.price().amount(), command.customerId(),
        command.transactionType());
    redisAdapter.saveTransaction(transactionId, transaction, Duration.ofSeconds(15));
    log.info("Pre transaction created with transactionId {}", transactionId);
    return new PreTransactionResponse(transactionId);

  }

  @Override
  @Transactional
  public void completeTransaction(UUID transactionId) {
    log.info("Completing transaction with transactionId {}", transactionId);
    Transaction transaction = redisAdapter.getTransactionByTransactionId(transactionId);
    if (transaction == null) {
      log.warn("Transaction with transactionId {} not found", transactionId);
      throw new TransactionException(ErrorCode.TRANSACTION_NOT_FOUND);
    }
    Asset asset = assetPort.getCustomerCurrencyAsset(transaction.customerId());
    asset.checkIfNull(asset);

    if (transaction.transactionType() == TransactionType.DEPOSIT) {
      log.info("Depositing amount {} to asset with assetId {}", transaction.amount(), asset.getId());
      asset.addAssets(transaction.amount());
      log.info("Deposited amount {} to asset with assetId {}", transaction.amount(), asset.getId());
    } else {
      log.info("Withdrawing amount {} from asset with assetId {}", transaction.amount(), asset.getId());
      asset.checkIfSufficientBalance(transaction.amount());
      asset.reduceAssets(transaction.amount());
      log.info("Withdrawn amount {} from asset with assetId {}", transaction.amount(), asset.getId());
    }

    assetPort.updateAsset(asset);
    log.info("Transaction completed successfully with transactionId {}", transactionId);
  }
}
