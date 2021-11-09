package com.utn.tp.io.controller;

import com.utn.tp.io.model.ModelType;
import com.utn.tp.io.model.Product;
import com.utn.tp.io.service.ProductService;
import com.utn.tp.io.utils.BrownTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private BrownTable brownTable;

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

    @GetMapping("/id/{id}")
    public ResponseEntity<Product> getById(@PathVariable Integer id){
        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping("/scan/{scan}")
    public ResponseEntity<Product> getByScan(@PathVariable String scan){
        return ResponseEntity.ok(productService.getByScan(scan));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Integer id){
        productService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/scan/{scan}/stock/{movement}")
    public ResponseEntity<Object> updateStock(@PathVariable String scan, @PathVariable Integer movement){
        productService.updateStock(scan, movement);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/recalculation/parameters/{scan}")
    public ResponseEntity<Object> recalculationParameters(@PathVariable String scan){
        productService.updateLastDays(scan,30);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/suggestModel/{id}")
    public ResponseEntity<ModelType> suggestedModel(@PathVariable Integer id){
        ModelType m = productService.suggestedModel(id);
        return ResponseEntity.ok(m);
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
    @GetMapping("/check/pmodel")
    public ResponseEntity<List<Product>> getToCheckModelP(){
        return ResponseEntity.ok(productService.getToCheckModelP());
    }

    @GetMapping("/check/qmodel")
    public ResponseEntity<List<Product>> getToCheckModelQ(){
        return ResponseEntity.ok(productService.getToCheckModelQ());
    }

    @GetMapping("/getz/{ez}")
    public ResponseEntity<Double> getz(@PathVariable Double ez){
        return ResponseEntity.ok(this.brownTable.calculateZeta(ez));
    }

}
