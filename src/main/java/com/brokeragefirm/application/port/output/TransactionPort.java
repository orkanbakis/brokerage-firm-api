package com.brokeragefirm.application.port.output;

import com.brokeragefirm.domain.model.Transaction;
import java.time.Duration;
import java.util.UUID;

public interface TransactionPort {

  void saveTransaction(UUID transactionId, Transaction transaction, Duration duration);

  Transaction getTransactionByTransactionId(UUID transactionId);

  void deleteTransactionByTransactionId(UUID transactionId);
}
