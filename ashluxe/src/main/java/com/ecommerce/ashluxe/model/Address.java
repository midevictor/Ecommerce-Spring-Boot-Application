package com.ecommerce.ashluxe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Address {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)

    private Long addressId;

    @NotBlank
    @Size(min = 5, message= "Street must be at least 5 characters long")
    private String street;

    @NotBlank
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters long")
    private String buildingName;

    @NotBlank
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters long")
    private String city;

    @NotBlank
    @Size(min = 2, max = 50, message = "State must be between 2 and 50 characters long")
    private String state;

    @NotBlank
    @Size(min = 2, max = 50, message = "Country must be between 2 and 50 characters long")
    private String country;

    @NotBlank
    @Size(min = 5, max = 10, message = "Zip code must be between 5 and 10 characters long")
    private String zipCode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

    public Address( String street, String buildingName, String city, String state, String country, String zipCode) {

        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;

    }
}
