package com.guru.ecommerce.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@Table(name="addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name Must be at least 5 characters")
    private String street;

    @NotBlank
    @Size(min = 5, message = "building name Must be at least 5 characters")
    private String buildingName;

    @NotBlank
    @Size(min = 4, message = "city name Must be at least 4 characters")
    private String city;

    @NotBlank
    @Size(min =3 , message = "state name Must be at least  3 characters")
    private String state;


    @NotBlank
    @Size(min =3 , message = "country name Must be at least  3 characters")
    private String country;

    @NotBlank
    @NotNull
    @Size(min =6 , message = "Pin name Must be at least  6 characters")
    private String pinCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

}
