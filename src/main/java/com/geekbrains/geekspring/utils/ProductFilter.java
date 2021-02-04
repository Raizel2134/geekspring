package com.geekbrains.geekspring.utils;

import com.geekbrains.geekspring.entities.Product;
import com.geekbrains.geekspring.repositories.specifications.ProductSpecs;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

@Getter
public class ProductFilter {
    private Specification<Product> spec;
    private StringBuilder filterDefinition;

    public ProductFilter(Map<String, String> map) {
        this.spec = Specification.where(null);
        this.filterDefinition = new StringBuilder();
        if (map.containsKey("min") && !map.get("min").isEmpty()) {
            int minPrice = Integer.parseInt(map.get("min"));
            spec = spec.and(ProductSpecs.priceGreaterThanOrEq(minPrice));
            filterDefinition.append("&min=").append(minPrice);
        }
        if (map.containsKey("max") && !map.get("max").isEmpty()) {
            int maxPrice = Integer.parseInt(map.get("max"));
            spec = spec.and(ProductSpecs.priceLesserThanOrEq(maxPrice));
            filterDefinition.append("&max=").append(maxPrice);
        }
        if (map.containsKey("word") && !map.get("word").isEmpty()) {
            String title = map.get("word");
            spec = spec.and(ProductSpecs.titleContains(title));
            filterDefinition.append("&word=").append(title);
        }
    }

    @Override
    public String toString() {
        return this.filterDefinition.toString();
    }
}
