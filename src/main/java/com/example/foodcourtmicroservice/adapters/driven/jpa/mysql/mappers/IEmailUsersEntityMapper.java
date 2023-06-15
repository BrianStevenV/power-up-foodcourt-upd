package com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.mappers;

import com.example.foodcourtmicroservice.adapters.driven.jpa.mysql.entity.Order.EmailUsersEntity;
import com.example.foodcourtmicroservice.domain.model.Order.EmailUser;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IEmailUsersEntityMapper {
    EmailUser toEmailUser(EmailUsersEntity emailUsersEntity);
}
