package com.adproa3.microservicevoucher.service;

import com.adproa3.microservicevoucher.model.DTO.VoucherDTO;
import com.adproa3.microservicevoucher.model.*;
import com.adproa3.microservicevoucher.model.DTO.*;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface VoucherService {
    public CompletableFuture<VoucherResponseDTO> useVoucherAsync(VoucherUsageRequestDTO request);
    public CompletableFuture<VoucherDTO> attachVoucherToUserAsync(UUID voucherId, String userId);
    public VoucherResponseDTO useVoucher(VoucherUsageRequestDTO request);
    public VoucherDTO attachVoucherToUser(UUID voucherId, String userId);
}
