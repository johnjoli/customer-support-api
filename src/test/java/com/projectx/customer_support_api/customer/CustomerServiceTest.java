package com.projectx.customer_support_api.customer;


import com.projectx.customer_support_api.mapper.CustomerMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    @Test
    @DisplayName("Успешный поиск клиента по ID")
    void findById_ShouldReturnCustomer_WhenCustomerExists() {
        Long customerId = 1L;
        Customer mockCustomer = new Customer(customerId, "Ivan Ivanov", "ivan@mail.com", "password", Role.ROLE_CUSTOMER);
        CustomerResponse expectedResponse = new CustomerResponse(customerId, "Ivan Ivanov", "ivan@mail.com");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));
        when(customerMapper.toResponse(mockCustomer)).thenReturn(expectedResponse);

        CustomerResponse actualResponse = customerService.findById(customerId);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        assertEquals(expectedResponse.fullName(), actualResponse.fullName());
        assertEquals(expectedResponse.email(), actualResponse.email());

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerMapper, times(1)).toResponse(mockCustomer);
    }

    @Test
    @DisplayName("Выброс исключения, если клиент по ID не найден")
    void findById_ShouldThrowCustomerNotFoundException_WhenCustomerDoesNotExists() {
        Long customerId = 999L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.findById(customerId);
        });

        verify(customerRepository, times(1)).findById(customerId);
        verifyNoInteractions(customerMapper);
    }
}
