package com.corebankingsystem.CustomerMs;

import com.corebankingsystem.CustomerMs.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;
    @Mock
    private RestTemplate restTemplateMock;

    @BeforeEach
    void setUp() {
        // Creamos el mock de RestTemplate
        restTemplateMock = mock(RestTemplate.class);
        // Inicializamos la clase AccountService con el mock
        accountService = new AccountService(restTemplateMock);
    }

    @Test
    void testHasActiveAccounts_ShouldReturnTrue_WhenAccountsExist() {
        // Preparamos los datos del mock
        Long customerId = 1L;
        List<?> mockResponse = List.of("account1", "account2");  // Simulamos que hay cuentas activas

        // Configuramos el mock para que el exchange retorne una respuesta con las cuentas
        when(restTemplateMock.exchange(
                eq("http://localhost:8087/accounts/customer/1"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class)
        )).thenReturn(ResponseEntity.ok(mockResponse));

        // Llamamos al método hasActiveAccounts y verificamos el resultado
        boolean hasActiveAccounts = accountService.hasActiveAccounts(customerId);
        assertTrue(hasActiveAccounts);  // Se espera que devuelva true
    }

    @Test
    void testHasActiveAccounts_ShouldReturnFalse_WhenNoAccountsExist() {
        // Preparamos los datos del mock
        Long customerId = 1L;
        List<?> mockResponse = List.of();  // Simulamos que no hay cuentas

        // Configuramos el mock para que el exchange retorne una respuesta vacía
        when(restTemplateMock.exchange(
                eq("http://localhost:8087/accounts/customer/1"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class)
        )).thenReturn(ResponseEntity.ok(mockResponse));

        // Llamamos al método hasActiveAccounts y verificamos el resultado
        boolean hasActiveAccounts = accountService.hasActiveAccounts(customerId);
        assertFalse(hasActiveAccounts);  // Se espera que devuelva false
    }

    @Test
    void testHasActiveAccounts_ShouldReturnFalse_WhenHttpClientErrorException() {
        // Preparamos los datos del mock
        Long customerId = 1L;

        // Configuramos el mock para que el exchange lance una excepción HttpClientErrorException
        when(restTemplateMock.exchange(
                eq("http://localhost:8087/accounts/customer/1"),
                eq(HttpMethod.GET),
                isNull(),
                eq(List.class)
        )).thenThrow(HttpClientErrorException.class);

        // Llamamos al método hasActiveAccounts y verificamos el resultado
        boolean hasActiveAccounts = accountService.hasActiveAccounts(customerId);
        assertFalse(hasActiveAccounts);  // Se espera que devuelva false cuando haya un error HTTP
    }
}
