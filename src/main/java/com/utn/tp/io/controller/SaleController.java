package com.utn.tp.io.controller;

import com.utn.tp.io.model.Sale;
import com.utn.tp.io.service.SaleService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private SaleService saleService;

    @Autowired
    public SaleController (SaleService saleService){
        this.saleService = saleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getById(@PathVariable Integer id){
        try {
            Sale sale = saleService.getById(id);
            return ResponseEntity.ok(sale);
        }catch (HttpClientErrorException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<Object> add(@RequestBody Sale sale){
        Sale newSale = this.saleService.add(sale);
        return ResponseEntity.created(URI.create("/sales"+newSale.getId())).build();
    }

    @PutMapping("/{id}/product/{id}")
    public ResponseEntity<String> addProductToSale(@PathVariable Integer id, @PathVariable Integer idProduct){
        Sale sale = this.saleService.addProductToSale(id, idProduct);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("The sale has been modified");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id){
        this.saleService.delete(id);
        return ResponseEntity.accepted().build();
    }


    /*(MODELO Q) Verificar al momento de la factura si el stock llega al punto de reorden para notificar*/
    @PutMapping("/{id}/product/{id}")
    public ResponseEntity<String> addProductToSaleAndVerify(@PathVariable Integer id, @PathVariable Integer idProduct){
        Sale sale = this.saleService.addProductToSaleVerify(id, idProduct);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("The sale has been modified");
    }


}
