package com.utn.tp.io.controller;

import com.utn.tp.io.model.Supplier;
import com.utn.tp.io.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService){
        this.supplierService=supplierService;
    }

    @PostMapping("/")
    public ResponseEntity<Object> addSupplier(@RequestBody Supplier supplier){
        Supplier newSupplier = supplierService.add(supplier);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newSupplier.getId())
                .toUri();
        return new ResponseEntity<>(location,(HttpStatus.CREATED));
    }

    @GetMapping("/")
    public ResponseEntity<List<Supplier>> allSuppliers() {
        return ResponseEntity.ok(supplierService.getAll());
    }

    private ResponseEntity<List<Supplier>> response(Page page) {

        HttpStatus httpStatus = page.getContent().isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return ResponseEntity.
                status(httpStatus).
                header("X-Total-Count", Long.toString(page.getTotalElements()))
                .header("X-Total-Pages", Long.toString(page.getTotalPages()))
                .body(page.getContent());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Supplier> supplierById(@PathVariable("id") Integer id){
        Supplier supplier = supplierService.getById(id);
        return ResponseEntity.ok(supplier);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable(name = "id") Integer id) {
            return supplierService.deleteById(id);
    }
}
