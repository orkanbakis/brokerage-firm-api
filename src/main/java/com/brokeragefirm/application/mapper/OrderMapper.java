package com.brokeragefirm.application.mapper;

import com.brokeragefirm.adapter.persistence.entity.OrderEntity;
import com.brokeragefirm.domain.enums.Currency;
import com.brokeragefirm.domain.model.Order;
import com.brokeragefirm.domain.valueobjects.Price;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface OrderMapper {

  @Mapping(target = "price", ignore = true)
  OrderEntity toEntity(Order order);


  @Mapping(target = "price", ignore = true)
  Order toModel(OrderEntity orderEntity);

  @AfterMapping
  default void setPrice(@MappingTarget OrderEntity orderEntity, Order order) {
    orderEntity.setPrice(order.getPrice().amount());
  }

  @AfterMapping
  default void setPrice(@MappingTarget Order order, OrderEntity orderEntity) {
    order.setPrice(new Price(orderEntity.getPrice(), Currency.TRY));
  }

}
