package com.utn.tp.io.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Date;

@Entity(name = "bills")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bill")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Sale> sales;

    private Date date;
    private Double total;

}
