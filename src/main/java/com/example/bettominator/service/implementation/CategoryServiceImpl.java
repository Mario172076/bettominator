package com.example.bettominator.service.implementation;

import com.example.bettominator.model.Category;
import com.example.bettominator.model.SportsGame;
import com.example.bettominator.model.exceptions.CategoryIsUsedException;
import com.example.bettominator.model.exceptions.CategoryNameException;
import com.example.bettominator.model.exceptions.CategoryNotFoundException;
import com.example.bettominator.repostiroy.CategoryRepository;
import com.example.bettominator.repostiroy.GameRepository;
import com.example.bettominator.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final GameRepository gameRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, GameRepository gameRepository) {
        this.categoryRepository = categoryRepository;
        this.gameRepository = gameRepository;
    }


    @Override
    public Optional<Category> findById(Long id) {
        return this.categoryRepository.findById(id);
    }

    @Override
    public Category findByName(String name) {
        return this.categoryRepository.findCategoryByName(name);
    }


    @Override
    public List<Category> findCategoryByNameLike(String name) {
        String nameLike = name != null ? "%" + name + "%" : null;
        if (nameLike != null) {
            return this.categoryRepository.findCategoryByNameLikeIgnoreCase(nameLike);
        } else {
            return this.categoryRepository.findAll();
        }
    }

    @Override
    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Category create(String name, String description) {
        if (name == null || name.isEmpty()) {
            throw new CategoryNameException();
        }
        Category c = new Category(name, description);
        categoryRepository.save(c);
        return c;
    }

    @Override
    public Optional<Category> edit(Long id, String name, String description, MultipartFile image) throws IOException {
        Category c = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        c.setName(name);
        c.setDescription(description);
        if (image != null && !image.getName().isEmpty()) {
            byte[] bytes = image.getBytes();
            String base64Image = String.format("data:%s;base64,%s", image.getContentType(), Base64.getEncoder().encodeToString(bytes));
            c.setImageBase64(base64Image);
        } else {
            throw new IOException();
        }
        return Optional.of(this.categoryRepository.save(c));
    }

    @Override
    public Optional<Category> save(Category category, MultipartFile image) throws IOException {
        if (image != null && !image.getName().isEmpty()) {
            byte[] bytes = image.getBytes();
            String base64Image = String.format("data:%s;base64,%s", image.getContentType(), Base64.getEncoder().encodeToString(bytes));
            category.setImageBase64(base64Image);
        } else {
            throw new IOException();
        }
        return Optional.of(this.categoryRepository.save(category));
    }


    @Override
    public boolean deleteCategoryById(Long id) throws CategoryIsUsedException {
        Optional<Category> cat = this.categoryRepository.findById(id);
        List<SportsGame> games = this.gameRepository.findAll();
        for (SportsGame sp : games) {
            if (sp.getCategory().getId().equals(cat.get().getId()))
                return false;
        }
        this.categoryRepository.deleteById(id);
        return true;
    }

    @Override
    public void delete(String name) {
        if (name == null || name.isEmpty()) {
            throw new CategoryNotFoundException(name);
        }
        this.categoryRepository.deleteByName(name);

    }

    @Override
    public void deleteById(Long id) {
        this.categoryRepository.deleteById(id);
    }


}
