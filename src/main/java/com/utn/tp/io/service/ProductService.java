package com.utn.tp.io.service;

import com.utn.tp.io.model.Product;
import com.utn.tp.io.model.Sale;
import com.utn.tp.io.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SaleService saleService;

    @Autowired
    public ProductService(ProductRepository productRepository, SaleService saleService) {
        this.productRepository = productRepository;
        this.saleService = saleService;
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
        if (p.getStock() < 0) {
            p.setStock(0);
        }
        productRepository.save(p);
    }

    public void updateLastDays(String scan, Integer quantityDays) {
        /*Obtengo el producto*/
        Product product = getByScan(scan);

        /*Calcula la fecha de hoy*/
        Date from = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        /*Calcula la fecha de hace 30 días*/
        Date to = Date.from(LocalDate.now().minusDays(quantityDays).atStartOfDay(ZoneId.systemDefault()).toInstant());

        /* Obtengo las ventas de los ultimos 30 días del producto*/
        List<Sale> salesInLastDays = saleService.getBetweenDatesAndProduct(from,to,product);

        double avgDemand = Sale.calculateAvgDemand(salesInLastDays, quantityDays);
        double disDemand = Sale.calculateDisDemand(salesInLastDays,avgDemand);
        double reorderPoint = Product.calculateReorderPoint(product);

        product.setAvgDemand(avgDemand);
        product.setDisDemand(disDemand);
        product.setReorderPoint(reorderPoint);

        this.productRepository.save(product);
    }
}
