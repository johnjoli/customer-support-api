package com.projectx.customer_support_api.mapper;


import com.projectx.customer_support_api.customer.Customer;
import com.projectx.customer_support_api.customer.CustomerCreateRequest;
import com.projectx.customer_support_api.customer.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {
    @Mapping(target = "id", ignore = true)
    Customer toEntity(CustomerCreateRequest request);

    CustomerResponse toResponse(Customer customer);
}