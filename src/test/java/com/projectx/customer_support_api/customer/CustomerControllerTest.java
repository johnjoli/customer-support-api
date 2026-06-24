package com.projectx.customer_support_api.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CustomerService customerService;

    @Test
    @DisplayName("GET /api/v1/customers должен возвращать список клиентов со статусом 200 OK")
    void findAll_ShouldReturnListAnd200() throws Exception {
        List<CustomerResponse> expectedList = List.of(
                new CustomerResponse(1L, "Джон Доу", "john@test.com"),
                new CustomerResponse(2L, "Джейн Доу", "jane@test.com")
        );
        when(customerService.findAll()).thenReturn(expectedList);

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].fullName").value("Джон Доу"))
                .andExpect(jsonPath("$[1].email").value("jane@test.com"));

        verify(customerService, times(1)).findAll();
    }

    @Test
    @DisplayName("POST /api/v1/customers с кривыми данными должен возвращать 400 Bad Request и карту ошибок валидации")
    void create_ShouldReturn400_WhenDataIsInvalid() throws Exception {

        CustomerCreateRequest invalidRequest = new CustomerCreateRequest("", "a");

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.email").exists())
                .andExpect(jsonPath("$.errors.fullName").exists());

        verifyNoInteractions(customerService);
    }

    @Test
    @DisplayName("GET /api/v1/customers/{id} должен возвращать 404 Not Found, если сервис выбросил исключение")
    void findById_ShouldReturn404_WhenNotFound() throws Exception {
        Long nonExistingId = 999L;
        when(customerService.findById(nonExistingId)).thenThrow(new CustomerNotFoundException(nonExistingId));

        mockMvc.perform(get("/api/v1/customers/{id}", nonExistingId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Customer with id 999 not found"));

        verify(customerService, times(1)).findById(nonExistingId);
    }
}
