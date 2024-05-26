package com.adproa3.microservicevoucher.model.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;
import java.time.*;
@Setter @Getter
public class VoucherDTO {
    private UUID voucherId;
    private String voucherName;
    private int voucherAmount;
    private String termsCondition;
    private int requiredSpending;
    private int quota;
    private int used;
    private LocalDate startDate;
    private LocalDate endDate;
    private String attachedUser;
}
