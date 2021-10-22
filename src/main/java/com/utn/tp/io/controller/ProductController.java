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
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity createProduct(@RequestBody Product product) throws Exception {
        Product p = productService.add(product);

        if(p.getModelType() == ModelType.P_MODEL && p.getReviewPeriod() == null)
            throw new Exception("Model P requires a review period to be specified.");
        else if (p.getModelType() == ModelType.Q_MODEL && p.getReviewPeriod() == null)
            throw new Exception("Model Q requires a reorder point to be specified.");       //Is required because is a new product, then is automatically recalculated

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
    public ResponseEntity deleteProduct(@PathVariable Integer id){
        productService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{scan}")
    public ResponseEntity updateStock(@PathVariable String scan, @RequestBody Integer movement){
        productService.updateStock(scan, movement);
        return ResponseEntity.accepted().build();
    }

/*    @PutMapping("/{id}")
    public ResponseEntity updateProduct(@PathVariable Integer id, @RequestBody Product product){
        productService.updateProduct(id, product);
        return ResponseEntity.accepted().build();
    }*/
}