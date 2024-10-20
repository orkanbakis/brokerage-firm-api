package com.brokeragefirm.application.port.input.command;

import com.brokeragefirm.domain.enums.TransactionType;
import com.brokeragefirm.domain.valueobjects.Price;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateTransactionCommand(
    @NotNull TransactionType transactionType,
    @NotNull Price price,
    @NotNull UUID customerId
) {

}
