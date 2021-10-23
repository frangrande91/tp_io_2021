package com.utn.tp.io.service;

import com.utn.tp.io.exception.RestResponseEntity;
import com.utn.tp.io.exception.SupplierNotExistsException;
import com.utn.tp.io.model.Bill;
import com.utn.tp.io.model.Supplier;
import com.utn.tp.io.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository){
        this.supplierRepository=supplierRepository;
    }

    public Supplier add(Supplier supplier){
        return supplierRepository.save(supplier);
    }

    public Page<Supplier> getAll(Pageable pageable){
        return supplierRepository.findAll(pageable);
    }

    public Supplier getById(Integer id) {
        return this.supplierRepository.findById(id).orElseThrow(()-> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<?> deleteById(Integer id){
        if (getById(id)!= null){
            supplierRepository.deleteById(id);
            return new ResponseEntity<>("has been deleted successfully.", HttpStatus.OK);
        }
        else {
            throw new SupplierNotExistsException("No Supplier was found by that id");
        }
    }
}
