package com.corebankingsystem.CustomerMs;

import com.corebankingsystem.CustomerMs.model.Customer;
import com.corebankingsystem.CustomerMs.repository.CustomerRepository;
import com.corebankingsystem.CustomerMs.service.AccountService;
import com.corebankingsystem.CustomerMs.service.CustomerValidationService;
import com.corebankingsystem.CustomerMs.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {


    @InjectMocks
    private CustomerServiceImpl customerServiceimpl;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerValidationService customerValidationService;
    @Mock
    private AccountService accountService;

    private Customer[] customersArray;

    private Long existingCustomerId = 1L;
    private Long nonExistingCustomerId = 99L;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        customersArray = new Customer[]{
                new Customer(1L, "Marcos", "Martinez", "78580311","jmartinez@gmail.com"),
                new Customer(2L, "Roberto", "Figueroa", "04536666", "rfigueroa@gmail.com"),
                new Customer(3L, "Steve", "Palomino", "45789999", "steve@gmail.com")
        };
    }

    @Test
    public void testValidateAndCreateCustomer_Success() {
        Customer customer = customersArray[0];
        customer.setFirstName("Marcos");
        customer.setLastName("Martinez");
        customer.setDni("78580311");
        customer.setEmail("jmartinez@gmail.com");

        when(customerRepository.findByDni("78580311")).thenReturn(Optional.empty());
        when(customerRepository.save(customer)).thenReturn(customer);

        ResponseEntity<Object> response = customerServiceimpl.createCustomer(customer);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(customer, response.getBody());
    }

    @Test
    public void testValidateAndCreateCustomer_DniAlreadyExists() {
        Customer newCustomer = new Customer(null, "Carlos", "Gomez", "12345678", "carlos@gmail.com");

        when(customerRepository.findByDni(anyString())).thenReturn(Optional.of(customersArray[0]));

        ResponseEntity<Object> response = customerServiceimpl.createCustomer(newCustomer);

        assertEquals(409, response.getStatusCodeValue());
        Map<String, String> error = (Map<String, String>) response.getBody();
        assertTrue(error.containsKey("message"));
        assertEquals("El DNI ya existe", error.get("message"));
    }

    @Test
    public void testGetCustomers() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customersArray));

        List<Customer> customers = customerServiceimpl.getCustomers();

        assertNotNull(customers);
        assertEquals(3, customers.size());
        assertEquals("Marcos", customers.get(0).getFirstName());
    }

    @Test
    public void testGetCustomerById_Found() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customersArray[0]));

        Optional<Customer> customer = customerServiceimpl.getCustomerById(1L);

        assertTrue(customer.isPresent());
        assertEquals("Marcos", customer.get().getFirstName());
    }

    @Test
    public void testGetCustomerById_NotFound() {
        when(customerRepository.findById(4L)).thenReturn(Optional.empty());

        Optional<Customer> customer = customerServiceimpl.getCustomerById(4L);

        assertFalse(customer.isPresent());
    }


    @Test
    void updateCustomer_ShouldReturnNotFound_WhenCustomerDoesNotExist() {

        Customer customerToUpdate = new Customer(99L, "Juan", "Perez", "12345678", "juanperez@gmail.com");
        when(customerServiceimpl.getCustomerById(nonExistingCustomerId)).thenReturn(Optional.empty());
        ResponseEntity<?> response = customerServiceimpl.updateCustomer(nonExistingCustomerId, customerToUpdate);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void updateCustomer_ShouldReturnUpdatedCustomer_WhenUpdateIsSuccessful() {

        Customer existingCustomer = new Customer(1L, "Marcos", "Martinez", "78580311", "jmartinez@gmail.com");
        Customer updatedCustomer = new Customer(existingCustomerId, "Marcos Updated", "Martinez", "78580311", "jmartinez@gmail.com");
        when(customerServiceimpl.getCustomerById(existingCustomerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(existingCustomer)).thenReturn(existingCustomer);
        ResponseEntity<?> response = customerServiceimpl.updateCustomer(existingCustomerId, existingCustomer);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(existingCustomer, response.getBody());
    }

    @Test
    public void testDeleteCustomer_CustomerNotFound() {
        Long customerId = 99L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        customerServiceimpl.deleteCustomer(customerId);
        verify(customerRepository, times(0)).deleteById(customerId);
    }
}
