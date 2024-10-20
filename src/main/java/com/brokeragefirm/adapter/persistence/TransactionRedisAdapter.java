package com.brokeragefirm.adapter.persistence;

import com.brokeragefirm.application.port.output.TransactionPort;
import com.brokeragefirm.domain.model.Transaction;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionRedisAdapter implements TransactionPort {

  private final RedisTemplate<UUID, Transaction> redisTemplate;

  @Override
  public void saveTransaction(UUID transactionId, Transaction transaction, Duration duration) {
    log.info("Caching transaction with transactionId {}", transactionId);
    redisTemplate.opsForValue().set(transactionId, transaction, duration);
    log.info("Transaction cached successfully with transactionId {}", transactionId);
  }

  @Override
  public Transaction getTransactionByTransactionId(UUID transactionId) {
    log.info("Getting transaction with transactionId {}", transactionId);
    return redisTemplate.opsForValue().get(transactionId);
  }

  @Override
  public void deleteTransactionByTransactionId(UUID transactionId) {
    log.info("Deleting transaction with transactionId {}", transactionId);
    redisTemplate.delete(transactionId);
    log.info("Transaction deleted successfully with transactionId {}", transactionId);
  }
}
