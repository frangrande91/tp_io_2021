package com.utn.tp.io.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import com.utn.tp.io.exception.SupplierNotExistsException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    @NotNull(message = "The phoneNumber cannot be null.")
    private Integer phoneNumber;

    @Positive(message = "The lead time should be positive.")
    @NotNull(message = "The lead time cannot be null.")
    private Integer leadTime;

    private Boolean isPresale; //If it is presale is true else false;

    @Positive(message = "The lead time should be positive.")
    @NotNull(message = "The lead time cannot be null.")
    private Integer reviewPeriod;

    private Date lastReview;

    //Retorna true si es momento de hacer la revisión
    public Boolean isRevisonPeriod(){

        if(this.isPresale) {
            //Calcula la fecha de hoy
            Date now = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

            //Calculo la cantidad de días entre la fecha actual y la fecha de la última revisión
            long diffInMillies = Math.abs(now.getTime() - this.lastReview.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            return this.reviewPeriod - (int) diff > 0;
        }
        else
            return false;
    }
}