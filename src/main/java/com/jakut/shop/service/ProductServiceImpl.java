package com.jakut.shop.service;

import com.jakut.shop.model.Product;
import com.jakut.shop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(final Product product) {
        productRepository.save(product);
        return product;
    }

    @Override
    public Product updateProduct(final Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(final Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public Long numberOfProducts() {
        return productRepository.count();
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
}
