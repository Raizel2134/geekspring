package com.geekbrains.geekspring.services;

import com.geekbrains.geekspring.entities.Product;
import com.geekbrains.geekspring.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return (List<Product>)(productRepository.findAll());
    }

    public List<Product> getAllProductsWithFilter(Specification<Product> productSpecs) {
        return (List<Product>)(productRepository.findAll(productSpecs));
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Page<Product> getAllProductsByPage(int pageNumber, int pageSize) {
        return productRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    public Page<Product> getProductsWithPagingAndFiltering(int pageNumber, int pageSize, Specification<Product> productSpecification) {
        return productRepository.findAll(productSpecification, PageRequest.of(pageNumber, pageSize));
    }

    public int getCountProductsWithFiltering(Specification<Product> productSpecification) {
        return productRepository.findAll(productSpecification).size();
    }

    public boolean isProductWithTitleExists(String productTitle) {
        return productRepository.findOneByTitle(productTitle) != null;
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @SuppressWarnings("uncheked")
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void deleteProduct(Long id) {
        productRepository.delete(findById(id));
    }
}
