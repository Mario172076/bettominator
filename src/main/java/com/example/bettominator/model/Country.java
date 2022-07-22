package com.example.bettominator.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "image")
    @Lob
    private String imageBase64;

    public Country(String name) {
        this.name = name;
    }

    public Country() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
