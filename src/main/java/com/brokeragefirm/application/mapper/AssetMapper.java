package com.brokeragefirm.application.mapper;

import com.brokeragefirm.adapter.persistence.entity.AssetEntity;
import com.brokeragefirm.domain.model.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface AssetMapper {

  AssetEntity toEntity(Asset order);

  Asset toModel(AssetEntity orderEntity);

}
