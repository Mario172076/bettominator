package com.example.bettominator.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    private String email;

    @Column(length = 4000)
    private String message;

    public Contact() {
    }

    public Contact(String name, String phone, String email, String message) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.message = message;
    }

}
