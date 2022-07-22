package com.example.bettominator.repostiroy;


import com.example.bettominator.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findContactsByNameLikeIgnoreCase(String name);

    List<Contact> findContactsByPhoneLike(String phone);

    List<Contact> findContactsByEmailLikeIgnoreCase(String email);
}
