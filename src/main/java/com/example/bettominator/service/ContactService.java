package com.example.bettominator.service;


import com.example.bettominator.model.Contact;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ContactService {


    List<Contact> findAll();

    Optional<Contact> findById(Long id);

    List<Contact> findContactsByNameLikeIgnoreCaseAndEmailLikeIgnoreCaseAndPhoneLikeIgnoreCase(String name);

    Contact create(String name, String phone, String email, String message);

    Optional<Contact> save(Contact contact) throws IOException;

    void deleteById(Long id);


}
