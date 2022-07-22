package com.example.bettominator.service;

import com.example.bettominator.model.Category;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CategoryService {


    Optional<Category> findById(Long id);

    Category findByName(String name);

    List<Category> findCategoryByNameLike(String name);

    List<Category> findAll();

    Category create(String name, String description);

    Optional<Category> edit(Long id, String name, String description, MultipartFile image) throws IOException;

    Optional<Category> save(Category category, MultipartFile image) throws IOException;

    boolean deleteCategoryById(Long id);

    void delete(String name);

    void deleteById(Long id);

}
