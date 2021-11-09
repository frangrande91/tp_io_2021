package com.utn.tp.io.model;

import com.utn.tp.io.utils.BrownTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@Entity(name = "products")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product")
    private Integer id;

    @NotBlank(message = "The scan code cannot be null or whitespace.")
    @Column(unique = true)
    private String scan;

    @NotBlank(message = "The model cannot be null or whitespace.")
    private String model;

    private String description;

    @ManyToOne
    private Supplier supplier;

    @PositiveOrZero(message = "The cost should be positive or zero.")
    @NotNull(message = "The cost cannot be null.")
    private Float costUnit;

    @PositiveOrZero(message = "The cost of preparing should be positive or zero.")
    @NotNull(message = "The cost of preparing cannot be null.")
    private Float costOfPreparing;

    @PositiveOrZero(message = "The cost of storage cost should be positive or zero.")
    @NotNull(message = "The storage cost of preparing cannot be null.")
    private Float storageCost;

    @PositiveOrZero(message = "The stock should be positive or zero.")
    @NotNull(message = "The stock cannot be null.")
    private Integer stock;

    //@NotNull(message = "The model type must be specified. It cannot be null.")
    private ModelType modelType;

    @Positive(message = "The service level should be positive.")
    @Max(value = 1, message = "The level service cannot be upper 100%.")
    private Double serviceLevel;

    private Double avgDemand;      //Average demand
    private Double disDemand;      //Dispersion of demand
    private Double reorderPoint;   //If modelType is Q this should be != NULL
    private ZoneProduct zone;

    public static Double calculateReorderPoint(Product product) {
        if(product.modelType.equals(ModelType.Q_MODEL)) {
            Double Q = Math.sqrt(((2*product.getAvgDemand()* product.getCostOfPreparing())/product.getStorageCost()));
            System.out.println("Q: "+Q);
            Double oL = Math.sqrt(product.getSupplier().getReviewPeriod()) * product.getDisDemand();
            System.out.println("oL: "+oL);
            Double eX = (1 - product.getServiceLevel())*(Q/oL);
            System.out.println("EX: "+eX);
            BrownTable brownTable = new BrownTable();
            Double z = brownTable.calculateZeta(eX);
            return (product.getAvgDemand()*product.getSupplier().getLeadTime()) + (z*oL);
        }
        return null;
    }







}
