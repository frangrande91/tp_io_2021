package com.utn.tp.io.service;

import com.utn.tp.io.model.Product;
import com.utn.tp.io.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product add(Product product) {
        return productRepository.save(product);
    }

    public Product getById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }

    public Product getByScan(String scan) {
        return productRepository.findByScan(scan);
    }

    public void updateStock(String scan, Integer movement) {
        Product p = getByScan(scan);
        p.setStock(p.getStock() + movement);
        if (p.getStock() < 0)
            p.setStock(0);
        productRepository.save(p);
    }

/*    public void updateProduct(Integer id, Product product) {
        Product p = getById(id);
    }*/
}
