package com.example.bettominator.service.implementation;


import com.example.bettominator.model.Contact;
import com.example.bettominator.model.exceptions.ContactParametarsException;
import com.example.bettominator.repostiroy.ContactRepository;
import com.example.bettominator.service.ContactService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }


    @Override
    public List<Contact> findAll() {
        return this.contactRepository.findAll();
    }

    @Override
    public Optional<Contact> findById(Long id) {
        return this.contactRepository.findById(id);
    }

    @Override
    public List<Contact> findContactsByNameLikeIgnoreCaseAndEmailLikeIgnoreCaseAndPhoneLikeIgnoreCase(String text) {
        String textLike = text != null ? "%" + text + "%" : null;
        Set<Contact> contactSet = new HashSet<>();
        if (textLike != null) {
            contactSet.addAll(this.contactRepository.findContactsByNameLikeIgnoreCase(textLike));
            contactSet.addAll(this.contactRepository.findContactsByEmailLikeIgnoreCase(textLike));
            contactSet.addAll(this.contactRepository.findContactsByPhoneLike(textLike));
        }

        if (contactSet.size() != 0) {
            return contactSet.stream().toList();
        } else {
            return this.contactRepository.findAll();
        }

    }

    @Override
    public Contact create(String name, String phone, String email, String message) {
        if (name == null || name.isEmpty() || ((phone == null || phone.isEmpty()) && (email == null || email.isEmpty())) || message == null || message.isEmpty()) {
            throw new ContactParametarsException();
        }
        Contact c = new Contact(name, phone, email, message);
        contactRepository.save(c);
        return c;
    }

    @Override
    public Optional<Contact> save(Contact contact) throws IOException {
        return Optional.of(this.contactRepository.save(contact));
    }

    @Override
    public void deleteById(Long id) {
        this.contactRepository.deleteById(id);
    }



}
