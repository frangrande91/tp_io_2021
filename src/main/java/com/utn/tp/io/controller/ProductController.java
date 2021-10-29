package com.utn.tp.io.controller;

import com.utn.tp.io.model.ModelType;
import com.utn.tp.io.model.Product;
import com.utn.tp.io.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody Product product) throws Exception {
        Product p = productService.add(product);

        /*if(p.getModelType() == ModelType.P_MODEL && p.getReviewPeriod() == null)
            throw new Exception("Model P requires a review period to be specified.");
        else if (p.getModelType() == ModelType.Q_MODEL && p.getReviewPeriod() == null)
            throw new Exception("Model Q requires a reorder point to be specified."); */      //Is required because is a new product, then is automatically recalculated

        return ResponseEntity.created(
                URI.create("/product/" + p.getId()))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll(){
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Integer id){
        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping("/{scan}")
    public ResponseEntity<Product> getByScan(@PathVariable String scan){
        return ResponseEntity.ok(productService.getByScan(scan));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Integer id){
        productService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/stock/{scan}")
    public ResponseEntity<Object> updateStock(@PathVariable String scan, @RequestBody Integer movement){
        productService.updateStock(scan, movement);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/recalculation/parameters/{scan}")
    public ResponseEntity<Object> recalculationParameters(@PathVariable String scan){
        productService.updateLastDays(scan,30);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/suggestModel/{id}")
    public ResponseEntity<Object> suggestedModel(@PathVariable Integer id){
        productService.suggestedModel(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        productService.updateProduct(id, product);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/supplier/{idSupplier}")
    public ResponseEntity<List<Product>> getProductsBySupplier(@PathVariable Integer idSupplier){
        return ResponseEntity.ok(productService.getBySupplier(idSupplier));
    }

    //Retorna una lista de los productos que están en periodo de revision
    @GetMapping("/check")
    public ResponseEntity<List<Product>> getToCheck(){
        return ResponseEntity.ok(productService.getToCheck());
    }

}
