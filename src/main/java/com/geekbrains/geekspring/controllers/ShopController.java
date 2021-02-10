package com.geekbrains.geekspring.controllers;

import com.geekbrains.geekspring.entities.Product;
import com.geekbrains.geekspring.repositories.specifications.ProductSpecs;
import com.geekbrains.geekspring.services.ProductsService;
import com.geekbrains.geekspring.utils.ProductFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/shop")
public class ShopController {
    private static final int INITIAL_PAGE = 0;
    private static final int PAGE_SIZE = 5;

    private ProductsService productsService;

    @Autowired
    public void setProductsService(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    public String shopPage(Model model,
                           @RequestParam Map<String, String> requestParams,
                           @RequestParam(value = "page") Optional<Integer> page
    ) {
        final int currentPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        ProductFilter productFilter = new ProductFilter(requestParams);

        Page<Product> products = productsService.getProductsWithPagingAndFiltering(currentPage, PAGE_SIZE, productFilter.getSpec());

        model.addAttribute("products", products.getContent());
        model.addAttribute("page", currentPage);
        model.addAttribute("totalPage", products.getTotalPages());

        System.out.println(productFilter.toString());

        model.addAttribute("filters", productFilter.toString());
        return "shop-page";
    }

    @GetMapping("/product_info/{id}")
    public String productPage(Model model, @PathVariable(value = "id") Long id) {
        Product product = productsService.findById(id);
        model.addAttribute("product", product);
        return "product-page";
    }
}
