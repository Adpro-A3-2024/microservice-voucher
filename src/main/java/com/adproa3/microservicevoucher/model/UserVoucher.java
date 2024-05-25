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
import java.util.UUID;
import java.time.*;

@Getter @Setter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVoucher {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @NotNull
    private UUID userVoucherId;
    private String userId;
    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;
    private LocalDateTime usedAt;
}
