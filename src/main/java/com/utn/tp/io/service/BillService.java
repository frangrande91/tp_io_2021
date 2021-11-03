package com.utn.tp.io.service;

import com.utn.tp.io.model.Bill;
import com.utn.tp.io.model.Sale;
import com.utn.tp.io.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;
import java.util.List;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final ProductService productService;
    private final SaleService saleService;

    @Autowired
    public BillService(BillRepository billRepository, ProductService productService, SaleService saleService) {
        this.billRepository = billRepository;
        this.productService = productService;
        this.saleService = saleService;
    }

    public Bill add(Bill bill) {
        List<Sale> saleList = bill.getSales();
        for(Sale s : saleList) {
            saleService.add(s);
            productService.updateStock(s.getProduct().getScan(),s.getQuantity()*(-1));
        }
        return this.billRepository.save(bill);
    }

    public Bill getById(Integer id) {
        return this.billRepository.findById(id).orElseThrow(()-> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public List<Bill> getAll() {
        return this.billRepository.findAll();
    }

    public List<Bill> getBetweenDate(Date from, Date to) {
        return this.billRepository.findByDateBetween(from,to);
    }

    public void deleteById(Integer id) {
        this.billRepository.deleteById(id);
    }

    public Bill getByLastId() {
        return this.billRepository.findByLastId();
    }

}
