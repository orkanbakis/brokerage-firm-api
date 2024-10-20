package com.brokeragefirm.application.port.input;

import com.brokeragefirm.adapter.web.dto.PreTransactionResponse;
import com.brokeragefirm.application.port.input.command.CreateTransactionCommand;
import java.util.UUID;

public interface TransactionUseCase {

  PreTransactionResponse createPreTransaction(CreateTransactionCommand command);

  void completeTransaction(UUID transactionId);
}
