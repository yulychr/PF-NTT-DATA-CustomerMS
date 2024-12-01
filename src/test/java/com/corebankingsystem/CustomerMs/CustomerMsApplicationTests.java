package com.corebankingsystem.CustomerMs;


import com.corebankingsystem.CustomerMs.model.Customer;
import com.corebankingsystem.CustomerMs.repository.CustomerRepository;
import com.corebankingsystem.CustomerMs.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@SpringBootTest
class CustomerMsApplicationTests {

	@InjectMocks
	private CustomerServiceImpl customerServiceimpl;
	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private RestTemplate restTemplate;

	// Inyectar el mock en el servicio
	private Customer[] customersArray;

	private Long existingCustomerId = 1L;
	private Long nonExistingCustomerId = 99L;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

		// Simulamos un arreglo de clientes
		customersArray = new Customer[]{
				new Customer(1L, "Marcos", "Martinez", "78580311","jmartinez@gmail.com"),
				new Customer(2L, "Roberto", "Figueroa", "04536666", "rfigueroa@gmail.com"),
				new Customer(3L, "Steve", "Palomino", "45789999", "steve@gmail.com")
		};
	}

	@Test
	public void testValidateAndCreateCustomer_Success() {
		// Usando el cliente con datos válidos
		Customer customer = customersArray[0];
		customer.setFirstName("Marcos");
		customer.setLastName("Martinez");
		customer.setDni("78580311");
		customer.setEmail("jmartinez@gmail.com");

		// Simulamos que el DNI no existe en la base de datos
		when(customerRepository.findByDni("78580311")).thenReturn(Optional.empty());
		when(customerRepository.save(customer)).thenReturn(customer);

		ResponseEntity<Object> response = customerServiceimpl.createCustomer(customer);

		// Validamos que la respuesta tenga un código 201 (Created) y el cliente creado
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
	public void testValidateAndCreateCustomer_FirstNameEmpty() {
		// Usando el cliente con el primer nombre vacío
		Customer customer = customersArray[0];
		customer.setFirstName("");  // Nombre vacío
		customer.setLastName("Martinez");
		customer.setDni("78580311");
		customer.setEmail("jmartinez@gmail.com");

		ResponseEntity<Object> response = customerServiceimpl.createCustomer(customer);

		// Validamos que la respuesta tenga un código 400 (Bad Request) y el error en firstName
		assertEquals(400, response.getStatusCodeValue());
		Map<String, String> body = (Map<String, String>) response.getBody();
		assertTrue(body.containsKey("firstName"));
		assertEquals("El nombre es obligatorio", body.get("firstName"));
	}

	@Test
	public void testValidateAndCreateCustomer_LastNameEmpty() {
		// Usando el cliente con el apellido vacío
		Customer customer = customersArray[1];
		customer.setFirstName("Roberto");
		customer.setLastName("");  // Apellido vacío
		customer.setDni("04536666");
		customer.setEmail("rfigueroa@gmail.com");

		ResponseEntity<Object> response = customerServiceimpl.createCustomer(customer);

		// Validamos que la respuesta tenga un código 400 (Bad Request) y el error en lastName
		assertEquals(400, response.getStatusCodeValue());
		Map<String, String> body = (Map<String, String>) response.getBody();
		assertTrue(body.containsKey("lastName"));
		assertEquals("El apellido es obligatorio", body.get("lastName"));
	}

	@Test
	public void testValidateAndCreateCustomer_DniEmpty() {
		// Usando el cliente con el DNI vacío
		Customer customer = customersArray[2];
		customer.setFirstName("Steve");
		customer.setLastName("Palomino");
		customer.setDni("");  // DNI vacío
		customer.setEmail("steve@gmail.com");

		ResponseEntity<Object> response = customerServiceimpl.createCustomer(customer);

		// Validamos que la respuesta tenga un código 400 (Bad Request) y el error en dni
		assertEquals(400, response.getStatusCodeValue());
		Map<String, String> body = (Map<String, String>) response.getBody();
		assertTrue(body.containsKey("dni"));
		assertEquals("El DNI es obligatorio", body.get("dni"));
	}

	@Test
	public void testValidateAndCreateCustomer_DniInvalid() {
		// Usando el cliente con un DNI inválido (menos de 8 dígitos)
		Customer customer = customersArray[0];
		customer.setFirstName("Marcos");
		customer.setLastName("Martinez");
		customer.setDni("12345");  // DNI inválido (menos de 8 dígitos)
		customer.setEmail("jmartinez@gmail.com");

		ResponseEntity<Object> response = customerServiceimpl.createCustomer(customer);

		// Validamos que la respuesta tenga un código 400 (Bad Request) y el error en dni
		assertEquals(400, response.getStatusCodeValue());
		Map<String, String> body = (Map<String, String>) response.getBody();
		assertTrue(body.containsKey("dni"));
		assertEquals("El DNI debe tener 8 dígitos", body.get("dni"));
	}

	@Test
	public void testValidateAndCreateCustomer_EmailEmpty() {
		// Usando el cliente con el email vacío
		Customer customer = customersArray[1];
		customer.setFirstName("Roberto");
		customer.setLastName("Figueroa");
		customer.setDni("04536666");
		customer.setEmail("");  // Email vacío

		ResponseEntity<Object> response = customerServiceimpl.createCustomer(customer);

		// Validamos que la respuesta tenga un código 400 (Bad Request) y el error en email
		assertEquals(400, response.getStatusCodeValue());
		Map<String, String> body = (Map<String, String>) response.getBody();
		assertTrue(body.containsKey("email"));
		assertEquals("El email es obligatorio", body.get("email"));
	}

	@Test
	public void testValidateAndCreateCustomer_EmailInvalid() {
		Customer customer = customersArray[2];
		customer.setFirstName("Steve");
		customer.setLastName("Palomino");
		customer.setDni("45789999");
		customer.setEmail("invalidemail.com");
		ResponseEntity<Object> response = customerServiceimpl.createCustomer(customer);

		assertEquals(400, response.getStatusCodeValue());
		Map<String, String> body = (Map<String, String>) response.getBody();
		assertTrue(body.containsKey("email"));
		assertEquals("Formato de correo electrónico inválido", body.get("email"));
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
	public void testUpdateCustomer_Success() {
		Customer updatedCustomer = new Customer(1L, "Marcos", "Martinez", "78580311","marcos_updated@gmail.com");

		when(customerRepository.findById(1L)).thenReturn(Optional.of(customersArray[0]));
		when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

		ResponseEntity<Customer> response = customerServiceimpl.updateCustomer(1L, updatedCustomer);

		assertEquals(200, response.getStatusCodeValue());
		assertEquals("marcos_updated@gmail.com", response.getBody().getEmail());
	}

	@Test
	public void testUpdateCustomer_NotFound() {
		Customer updatedCustomer = new Customer(5L, "Marcos", "Martinez", "78580311", "marcos_updated@gmail.com");

		when(customerRepository.findById(5L)).thenReturn(Optional.empty());
		ResponseEntity<Customer> response = customerServiceimpl.updateCustomer(5L, updatedCustomer);
		assertEquals(404, response.getStatusCodeValue());
	}

	// Test de la eliminacion de un Customer que si existe
	@Test
	public void testDeleteCustomer_ExistingCustomer() {
		Long customerId = 1L;
		Customer customer = customersArray[0];
		when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
		customerServiceimpl.deleteCustomer(customerId);
		//verify(customerRepository, times(1)).deleteById(customerId);
	}

	// Test del caso de un Customer que no existe
	@Test
	public void testDeleteCustomer_CustomerNotFound() {
		Long customerId = 99L;

		when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
		customerServiceimpl.deleteCustomer(customerId);
		verify(customerRepository, times(0)).deleteById(customerId);
	}


}
