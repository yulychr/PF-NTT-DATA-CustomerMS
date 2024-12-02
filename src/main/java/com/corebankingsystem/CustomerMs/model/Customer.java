package com.corebankingsystem.CustomerMs.model;

import lombok.*;
import jakarta.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


@Data
@Entity
@Table(name="customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="firstName", nullable = false)
    @NotEmpty
    private String firstName;

    @Column(name="lastName", nullable = false)
    @NotEmpty
    private String lastName;

    @Column(name="dni", unique = true, nullable = false)
    @NotEmpty
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    private String dni;

    @Column(name="email", nullable = false)
    @Email
    @NotEmpty(message = "El email es obligatorio")
    private String email;


    public Customer(Long id, String firstName, String lastName, String dni, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dni = dni;
        this.email = email;
    }
    public Customer(){

    }
}
