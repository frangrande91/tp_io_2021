package com.utn.tp.io.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@Entity
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

    @NotBlank(message = "The name cannot be null or whitespace.")
    private String name;

    @NotBlank(message = "The model cannot be null or whitespace.")
    private String model;

    private String description;

    //private Supplier supplier;

    @PositiveOrZero(message = "The cost should be positive or zero.")
    @NotNull(message = "The cost cannot be null.")
    private Float costUnit;

    @PositiveOrZero(message = "The stock should be positive or zero.")
    @NotNull(message = "The stock cannot be null.")
    private Integer stock;

    @NotNull(message = "The model type must be specified. It cannot be null.")
    private ModelType modelType;

    @Positive(message = "The service level should be positive.")
    @Max(value = 100, message = "The level service cannot be upper 100%.")
    private Integer serviceLevel;

    private Integer avgDemand;      //Average demand
    private Integer disDemand;      //Dispersion of demand
    private Integer reorderPoint;   //If modelType is Q this should be != NULL
    private Integer reviewPeriod;   //If modelType is P this should be != NULL
}
