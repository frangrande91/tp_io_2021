package com.utn.tp.io.repository;

import com.utn.tp.io.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill,Integer> {
    List<Bill> findByDateBetween(Date from, Date to);
    @Query(value = "SELECT * FROM bills WHERE (SELECT max(id) from bills) = id", nativeQuery = true)
    Bill findByLastId();
}
