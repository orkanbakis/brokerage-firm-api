package com.brokeragefirm.domain.model;

import com.brokeragefirm.domain.enums.TransactionType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;


public record Transaction(
    BigDecimal amount,
    UUID customerId,
    TransactionType transactionType
) implements Serializable {

}
