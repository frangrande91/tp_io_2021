package com.utn.tp.io.repository;

import com.utn.tp.io.model.Product;
import com.utn.tp.io.model.Sale;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {
    List<Sale> findAllByDateBetweenAndProduct(Date from, Date to, Product product);
}
