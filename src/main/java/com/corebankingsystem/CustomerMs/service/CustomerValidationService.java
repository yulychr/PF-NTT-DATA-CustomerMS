package com.corebankingsystem.CustomerMs.service;

import com.corebankingsystem.CustomerMs.model.Customer;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerValidationService {
    public Map<String, String> validateCustomerFields(Customer customer) {
        List<Validation> validations = List.of(
                new Validation("firstName", isNullOrEmpty(customer.getFirstName()), "El nombre es obligatorio"),
                new Validation("lastName", isNullOrEmpty(customer.getLastName()), "El apellido es obligatorio"),
                new Validation("dni", isNullOrEmpty(customer.getDni()) || !customer.getDni().matches("\\d{8}"), "El DNI debe tener 8 dígitos"),
                new Validation("email", isNullOrEmpty(customer.getEmail()) || !customer.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$"), "Formato de correo electrónico inválido")
        );
        return validations.stream()
                .filter(validation -> validation.isInvalid)
                .collect(Collectors.toMap(Validation::getField, Validation::getMessage));
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    private static class Validation {
        String field;
        boolean isInvalid;
        String message;

        Validation(String field, boolean isInvalid, String message) {
            this.field = field;
            this.isInvalid = isInvalid;
            this.message = message;
        }

        String getField() {
            return field;
        }

        String getMessage() {
            return message;
        }
    }
}
