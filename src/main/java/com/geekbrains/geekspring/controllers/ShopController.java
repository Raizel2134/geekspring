package com.geekbrains.geekspring.controllers;

import com.geekbrains.geekspring.entities.Category;
import com.geekbrains.geekspring.entities.Product;
import com.geekbrains.geekspring.services.CategoryService;
import com.geekbrains.geekspring.services.ProductService;
import com.geekbrains.geekspring.utils.ProductFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ShopController {
    private static final int INITIAL_PAGE = 0;
    private static final int PAGE_SIZE = 10;

    private ProductService productService;
    private CategoryService categoryService;


    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public String searchProduct(Model model,
                                @RequestParam Map<String, String> requestParams,
                                @RequestParam(value = "page") Optional<Integer> page) {
        searchAndFilter(model, requestParams, page);
        return "products-list";
    }

    @RequestMapping(value = "/product-list", method = RequestMethod.GET)
    public String showProductList(Model model,
                                  @RequestParam Map<String, String> requestParams,
                                  @RequestParam(value = "page") Optional<Integer> page
    ) {
        searchAndFilter(model, requestParams, page);
        return "products-list";
    }

    private void searchAndFilter(Model model, @RequestParam Map<String, String> requestParams, @RequestParam("page") Optional<Integer> page) {
        final int currentPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        ProductFilter productFilter = new ProductFilter(requestParams);
        Page<Product> products = productService.getProductsWithPagingAndFiltering(currentPage, PAGE_SIZE, productFilter.getSpec());
        int countProduct = productService.getCountProductsWithFiltering(productFilter.getSpec());
        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("products", products.getContent());
        model.addAttribute("page", currentPage);
        model.addAttribute("totalPage", products.getTotalPages());
        model.addAttribute("countProduct", countProduct);
        model.addAttribute("categories", categories);
        model.addAttribute("filters", productFilter.toString());
        model.addAttribute("currentPage", currentPage);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String showProduct(Model model,
                              @PathVariable long id) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "product";
    }
}
