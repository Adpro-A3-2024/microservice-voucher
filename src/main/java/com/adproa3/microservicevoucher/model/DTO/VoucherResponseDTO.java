package com.adproa3.microservicevoucher.model.DTO;

import lombok.Getter;
import lombok.Setter;
@Getter@Setter
public class VoucherResponseDTO {
    private boolean success;
    private String message;
    private double totalAfterDiscount;
    private VoucherDTO voucher;

    public VoucherResponseDTO(boolean success, String message, double totalAfterDiscount, VoucherDTO voucher){
        this.success = success;
        this.message = message;
        this.totalAfterDiscount = totalAfterDiscount;
        this.voucher = voucher;
    }
}
