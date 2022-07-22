package com.example.bettominator.web;


import com.example.bettominator.model.Contact;
import com.example.bettominator.service.ContactService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/Contact")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public String addContactPage(Model model) {
        return "Contact";
    }

    @GetMapping("/contacts")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String contactsPage(@RequestParam(required = false) String error,
                               @RequestParam(required = false) String contactText,
                               Model model) {
        List<Contact> contacts;
        if (contactText == null) {
            contacts = this.contactService.findAll();
        } else {
            contacts = this.contactService.findContactsByNameLikeIgnoreCaseAndEmailLikeIgnoreCaseAndPhoneLikeIgnoreCase(contactText);
        }
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("Contacts", contacts);
        return "Contacts";
    }


    @DeleteMapping("/contacts/delete/{id}")
    public String deleteContact(@PathVariable Long id) {
        this.contactService.deleteById(id);
        return "redirect:/Contact/contacts?message=ContactDeletedSuccessfully";
    }


    @PostMapping
    public String saveContact(
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String email,
            @RequestParam String message,
            Model model) {
        try {
            Contact c = new Contact(name, phone, email, message);
            this.contactService.save(c);
            return "redirect:/Contact?message=ContactSendSuccessfully";
        } catch (IOException ex) {
            return "redirect:/Contact?error=" + ex.getLocalizedMessage();
        }
    }
}
