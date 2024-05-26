package com.adproa3.microservicevoucher.model.DTO;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Setter @Getter
public class AttachVoucherResponseDTO {
    private String userId;
    private UUID voucherId;
    private String voucherName;

    public AttachVoucherResponseDTO(String userId, UUID voucherId, String voucherName) {
        this.userId = userId;
        this.voucherId = voucherId;
        this.voucherName = voucherName;
    }
}
