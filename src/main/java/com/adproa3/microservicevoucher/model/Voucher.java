package com.adproa3.microservicevoucher.model;

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

@Entity
@Table(name = "vouchers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Getter @Setter
public class Voucher {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @NotNull
    private UUID voucherId;
    @NotBlank(message = "Nama voucher tidak boleh kosong.")
    private String voucherName;
    @NotNull(message = "Jumlah diskon voucher tidak boleh bernilai null.")
    private int voucherAmount;
    private String termsCondition;
    private int quota;
    private int used;
    private int requiredSpending;
    @NotNull(message = "Start Date voucher tidak boleh bernilai null.")
    private LocalDate startDate;
    @NotNull(message = "End Date voucher tidak boleh bernilai null.")
    private LocalDate endDate;
    @Version
    private int version;
    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserVoucher> userVouchers;
    public Voucher(String voucherName, int voucherAmount, int quota, String termsCondition,int requiredSpending, LocalDate startDate, LocalDate endDate){
        this.voucherName = voucherName;
        this.voucherAmount = voucherAmount;
        this.quota = quota;
        this.termsCondition = termsCondition;
        this.requiredSpending = requiredSpending;
        this.startDate = startDate;
        this.endDate = endDate;

    }
}

