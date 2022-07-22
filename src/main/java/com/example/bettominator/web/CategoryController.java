package com.example.bettominator.web;

import com.example.bettominator.model.Category;
import com.example.bettominator.model.exceptions.CategoryNotFoundException;
import com.example.bettominator.service.CategoryService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import java.io.IOException;
import java.util.List;

@Controller
@ControllerAdvice
@RequestMapping("/Categories")
public class CategoryController {

    private final CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;


    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getCategoryPage(@RequestParam(required = false) String error,
                                  @RequestParam(required = false) String catName,
                                  Model model) {
        List<Category> categories;
        if (catName == null) {
            categories = categoryService.findAll();
        } else {
            categories = this.categoryService.findCategoryByNameLike(catName);
        }
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("Categories", categories);
        return "Categories";
    }


    @DeleteMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        if (!this.categoryService.deleteCategoryById(id)) {
            return "redirect:/Categories?error=CategoryUsedAndCannotBeDeleted";
        } else {
            return "redirect:/Categories?message=CategoryDeletedSuccessfully";
        }
    }

    @GetMapping("/edit-form/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editCategoryPage(@PathVariable Long id, Model model) {
        if (this.categoryService.findById(id).isPresent()) {
            Category category = this.categoryService.findById(id).get();
            model.addAttribute("category", category);
            return "Add-Category";

        }
        return "redirect:/Categories?error=CategoryNotFound";
    }

    @GetMapping("/add-form")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addCategoryPage(Model model) {
        return "Add-Category";
    }


    @PostMapping("/add")
    @Secured("ROLE_ADMIN")
    public String saveCategory(
            @Valid Category category,
            BindingResult bindingResult,
            @RequestParam MultipartFile image,
            Model model
    ) {
        if (category.getId() != null) {
            try {
                this.categoryService.edit(category.getId(), category.getName(), category.getDescription(), image);
            } catch (IOException | CategoryNotFoundException e) {
                return "redirect:/Categories?error=" + e.getLocalizedMessage();
            }
        } else {
            try {
                this.categoryService.save(category, image);
            } catch (IOException | CategoryNotFoundException e) {
                return "redirect:/Categories?error=" + e.getLocalizedMessage();
            }
        }


        return "redirect:/Categories";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleFileUploadError(RedirectAttributes ra){
        System.out.println("Caught file upload error");
        ra.addAttribute("error", "Could not upload file bigger than 2MB");
        return "redirect:/Home";
    }
}
