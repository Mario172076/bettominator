package com.example.bettominator.web.rest;

import com.example.bettominator.model.Category;
import com.example.bettominator.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/Categories")
public class CategoryRestController {

    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> findAll() {
        return this.categoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id) {
        return this.categoryService.findById(id)
                .map(category -> ResponseEntity.ok().body(category))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Category> save(@Valid Category category,
                                         BindingResult bindingResult,
                                         @RequestParam MultipartFile image,
                                         Model model) throws IOException {
        return this.categoryService.save(category, image)
                .map(cnt -> ResponseEntity.ok().body(cnt))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<Category> edit(@PathVariable Long id, String name, String description, MultipartFile image) throws IOException {
        return this.categoryService.edit(id, name, description, image)
                .map(cnt -> ResponseEntity.ok().body(cnt))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        this.categoryService.deleteById(id);
        if (this.categoryService.findById(id).isEmpty()) return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }
}

