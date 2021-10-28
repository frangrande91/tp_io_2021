package com.utn.tp.io.service;

import com.utn.tp.io.model.Product;
import com.utn.tp.io.model.Sale;
import com.utn.tp.io.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductService productService;

    @Autowired
    public SaleService(SaleRepository saleRepository, ProductService productService){
        this.saleRepository = saleRepository;
        this.productService = productService;
    }

    public Sale getById(Integer id) {
        return this.saleRepository.getById(id);
    }

    public List<Sale> getBetweenDatesAndProduct(Date from, Date to, Product product) {
        return saleRepository.findAllByDateBetweenAndProduct(from,to,product);
    }

    public Sale add(Sale sale) {
        return this.saleRepository.save(sale);
    }

    public Sale addProductToSale(Integer id, Integer idProduct) {
        Sale sale = this.saleRepository.getById(id);
        Product product = this.productService.getById(idProduct);
        sale.setProduct(product);
        return this.saleRepository.save(sale);
    }

    public void delete(Integer id) {
        this.saleRepository.deleteById(id);
    }

}
