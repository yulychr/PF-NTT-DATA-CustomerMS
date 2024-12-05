package com.corebankingsystem.CustomerMs;

import com.corebankingsystem.CustomerMs.controller.CustomerController;
import com.corebankingsystem.CustomerMs.model.Customer;
import com.corebankingsystem.CustomerMs.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomerControllerTest {
    @Mock
    private MockMvc mockMvc;
    @Mock
    private CustomerService customerServiceMock;
    @InjectMocks
private CustomerController customerController;

    @BeforeEach
    void setUp() {
        customerServiceMock = mock(CustomerService.class);
        customerController = new CustomerController();
        customerController.customerService = customerServiceMock;
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void testCreateCustomer() throws Exception {

        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setDni("12345678");
        customer.setEmail("john.doe@example.com");

        when(customerServiceMock.createCustomer(Mockito.any(Customer.class)))
                .thenReturn(ResponseEntity.status(201).body(customer));

        mockMvc.perform(post("/customers/")
                        .contentType("application/json")
                        .content("{\"firstName\":\"John\", \"lastName\":\"Doe\", \"dni\":\"12345678\", \"email\":\"john.doe@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.dni").value("12345678"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(customerServiceMock, times(1)).createCustomer(Mockito.any(Customer.class));
    }

    @Test
    void testGetAllCustomers() throws Exception {

        Customer customer1 = new Customer();
        customer1.setFirstName("John");
        customer1.setLastName("Doe");
        customer1.setDni("12345678");
        customer1.setEmail("john.doe@example.com");

        Customer customer2 = new Customer();
        customer2.setFirstName("Jane");
        customer2.setLastName("Doe");
        customer2.setDni("87654321");
        customer2.setEmail("jane.doe@example.com");

        List<Customer> customers = List.of(customer1, customer2);
        when(customerServiceMock.getCustomers()).thenReturn(customers);
        mockMvc.perform(get("/customers/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
        verify(customerServiceMock, times(1)).getCustomers();
    }

    @Test
    void testGetCustomerById() throws Exception {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setDni("12345678");
        customer.setEmail("john.doe@example.com");
        when(customerServiceMock.getCustomerById(1L)).thenReturn(Optional.of(customer));
        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
        verify(customerServiceMock, times(1)).getCustomerById(1L);
    }

}
