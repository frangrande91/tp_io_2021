package com.utn.tp.io.service;

import com.utn.tp.io.model.Product;
import com.utn.tp.io.model.Sale;
import com.utn.tp.io.model.Supplier;
import com.utn.tp.io.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SaleService saleService;
    private final SupplierService supplierService;

    @Autowired
    public ProductService(ProductRepository productRepository, @Lazy SaleService saleService, SupplierService supplierService) {
        this.productRepository = productRepository;
        this.saleService = saleService;
        this.supplierService = supplierService;
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

    public List<Product> getBySupplier(Integer idSupplier){
        List<Product> products = new ArrayList<>();
        for (Product p : this.getAll()) {
            if(p.getSupplier().getId().equals(idSupplier)){
                products.add(p);
            }
        }
        return products;
    }


    public List<Product> getToCheck(){
        List<Supplier> revisionSuppliers = new ArrayList<Supplier>();
        List<Product> productsToCheck = new ArrayList<>();

        //Guardo en una lista a todos los proveedores que están en período de revision
        for(Supplier s : supplierService.getAll()){
            if(s.isRevisonPeriod()){
                revisionSuppliers.add(s);
            }
        }

        //Guardo en una lista a todos los productos que pertenecen a un proveedor que está en período de revisión
        for(Product p : this.getAll()){
            if(revisionSuppliers.contains(p.getSupplier())){
                productsToCheck.add(p);
            }
        }
        return productsToCheck;
    }
}
