package com.brokeragefirm.common.config.cache.properties;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "spring.data.redis")
@Configuration
@Getter
@Setter
public class RedisProperties {

  @NotEmpty
  private String host;
  @NotNull
  private Integer port;
}
