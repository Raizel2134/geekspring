package com.geekbrains.geekspring.controllers;

import com.geekbrains.geekspring.entities.Product;
import com.geekbrains.geekspring.entities.ProductImage;
import com.geekbrains.geekspring.services.CategoryService;
import com.geekbrains.geekspring.services.ImageSaverService;
import com.geekbrains.geekspring.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/product")
public class AdminProductController {
    private ProductService productService;
    private ImageSaverService imageSaverService;
    private CategoryService categoryService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setImageSaverService(ImageSaverService imageSaverService) {
        this.imageSaverService = imageSaverService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin-products-page";
    }

    @RequestMapping(value = "/add/", method = RequestMethod.GET)
    public String showAddProductForm(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("product", new Product());
        return "admin-products-page-create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String processProductAddForm(@Valid @ModelAttribute("product") Product product, BindingResult theBindingResult,
                                        Model model, @RequestParam("file") MultipartFile file) {
        if (productService.isProductWithTitleExists(product.getTitle())) {
            theBindingResult.addError(new ObjectError("product.title", "Товар с таким названием уже существует"));
        }

        if (theBindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "redirect:/admin/product/add";
        }
        if (!file.isEmpty()) {
            String pathToSavedImage = imageSaverService.saveFile(file);
            ProductImage productImage = new ProductImage();
            productImage.setPath(pathToSavedImage);
            productImage.setProduct(product);
            product.addImage(productImage);
            productService.saveProduct(product);
        }
        return "redirect:/admin/product";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String showEditProductForm(Model model, @PathVariable Long id) {
        Product product = productService.findById(id);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("product", product);
        return "admin-products-page-edit";
    }

    @RequestMapping(value = "/saveEdit", method = RequestMethod.POST)
    public String processProductEditForm(@Valid @ModelAttribute("product") Product product) {
        product.setUpdateAt(LocalDateTime.now());
        productService.saveProduct(product);
        return "redirect:/admin/product";
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
    public String removeProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/product";
    }
}
