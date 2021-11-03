package com.utn.tp.io.controller;

import com.utn.tp.io.model.Bill;
import com.utn.tp.io.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api//bill")
public class BillController {

    private final BillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getById(@PathVariable Integer id) {
        try {
            Bill bill = this.billService.getById(id);
            return ResponseEntity.ok(bill);
        } catch (HttpClientErrorException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{from}/{to}")
    public ResponseEntity<List<Bill>> getBetweenDates(@PathVariable Date from, @PathVariable Date to) {
        List<Bill> billList = this.billService.getBetweenDate(from,to);
        return ResponseEntity.ok(billList);
    }

    @GetMapping("/")
    public ResponseEntity<List<Bill>> getAll() {
        List<Bill> billList = this.billService.getAll();
        return ResponseEntity.ok(billList);
    }

    @GetMapping("/lastId")
    public ResponseEntity<Bill> getByLastId() {
        return ResponseEntity.ok(this.billService.getByLastId());
    }

    @PostMapping("/")
    public ResponseEntity<Object> add(@RequestBody Bill bill) {
        Bill billCreated = billService.add(bill);
        return ResponseEntity.created(URI.create("/bill/"+billCreated.getId())).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        billService.deleteById(id);
        return ResponseEntity.accepted().build();
    }

}
