package com.utn.tp.io.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Date;

@Entity
@Data
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany
    private List<Sale> sales;

    private Date date;
    private Double total;

}
