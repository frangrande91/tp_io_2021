package com.utn.tp.io.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(name = "id_sale")*/
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product")
    private Product product;

    private Integer quantity;

    private Float subtotal;

    private Date date;

    public static Double calculateDisDemand(List<Sale> saleList, Double avgDemand ) {
        double summation = 0.0;

        for(Sale s : saleList) {
            summation += Math.pow( ( s.getQuantity() - avgDemand ), 2) ;
        }

        return Math.sqrt(summation/avgDemand);
    }

    public static Double calculateAvgDemand(List<Sale> saleList, Integer quantityDays ) {
        double totalDemand = 0.0;
        System.out.println("Longitud: "+saleList.size());
        for(Sale s : saleList) {
            totalDemand += s.getQuantity();
        }

        return totalDemand/quantityDays;
    }



}
