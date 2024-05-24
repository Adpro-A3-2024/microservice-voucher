package com.adproa3.microservicevoucher.model.DTO;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Setter @Getter
public class VoucherUsageRequestDTO {
    private String userId;
    private UUID voucherId;
    private double cartTotal;
}
