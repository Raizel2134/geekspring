package com.geekbrains.geekspring.controllers;

import com.geekbrains.geekspring.entities.Category;
import com.geekbrains.geekspring.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController {
    private CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin-categories-page";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String editCategory(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "admin-categories-page-edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String saveChangeCategory(Model model, Category category) {
        categoryService.save(category);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "redirect:/admin/category";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addCategory(Model model) {
        model.addAttribute("category", new Category());
        return "admin-categories-page-create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String saveCategory(Model model, Category category) {
        categoryService.save(category);
        return "redirect:/admin/category";
    }
}
