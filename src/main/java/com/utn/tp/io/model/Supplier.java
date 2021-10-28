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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

@Data
@Entity(name = "suppliers")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {

    @Id
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "The name cannot be null or whitespace.")
    private String name;

    @Positive(message = "The phone Number should be positive.")
    @NotBlank(message = "The phoneNumber cannot be null or whitespace.")
    private Integer phoneNumber;

    private boolean isPresale; //If it is presale is true else false;

    @Positive(message = "The lead time should be positive.")
    @NotNull(message = "The lead time cannot be null.")
    private Integer leadTime;

    @Positive(message = "The lead time should be positive.")
    @NotNull(message = "The lead time cannot be null.")
    private Integer reviewPeriod;

    private Date lastReview;
}
