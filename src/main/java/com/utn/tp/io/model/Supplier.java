package com.utn.tp.io.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Supplier {

    @Id
    @GeneratedValue(strategy  = GenerationType.IDENTITY)

    private Integer id;
    @NotBlank(message = "The model cannot be null or whitespace.")
    private String name;
    @NotBlank(message = "The model cannot be null or whitespace.")
    private Integer phoneNumer;
}
