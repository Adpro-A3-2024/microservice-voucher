package com.adproa3.microservicevoucher.controller;

import com.adproa3.microservicevoucher.service.VoucherReadService;
import com.adproa3.microservicevoucher.service.VoucherWriteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import com.adproa3.microservicevoucher.model.*;
import com.adproa3.microservicevoucher.model.DTO.*;
import com.adproa3.microservicevoucher.service.VoucherService;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private VoucherReadService voucherReadService;
    @Autowired
    private VoucherWriteService voucherWriteService;

    @PostMapping("/use")
    public ResponseEntity<VoucherResponseDTO> useVoucher(@RequestBody @Valid VoucherUsageRequestDTO request) {
        CompletableFuture<VoucherResponseDTO> response = voucherService.useVoucherAsync(request);
        return ResponseEntity.ok(response.join());
    }

    @GetMapping
    public List<VoucherDTO> getAllVouchers() {
        return voucherReadService.getAllVouchers();
    }

    @PostMapping
    public ResponseEntity<VoucherDTO> createVoucher(@RequestBody @Valid VoucherDTO voucherDTO) {
        CompletableFuture<VoucherDTO> createdVoucher = voucherWriteService.createVoucherAsync(voucherDTO);
        return ResponseEntity.ok(createdVoucher.join());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VoucherDTO> updateVoucher(@PathVariable UUID id, @RequestBody @Valid VoucherDTO voucherDTO) {
        CompletableFuture<VoucherDTO> updatedVoucher = voucherWriteService.updateVoucherAsync(id, voucherDTO);
        return ResponseEntity.ok(updatedVoucher.join());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable UUID id) {
        CompletableFuture<Void> deletedVoucher = voucherWriteService.deleteVoucherAsync(id);
        deletedVoucher.join();
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VoucherDTO>> getAllVouchersForUser(@PathVariable String userId) {
        List<VoucherDTO> vouchers = voucherReadService.getAllVouchersForUser(userId);
        return ResponseEntity.ok(vouchers);
    }
    @PostMapping("/{voucherId}/attach")
    public ResponseEntity<VoucherDTO> attachVoucherToUser(@PathVariable UUID voucherId, @RequestBody AttachVoucherRequestDTO request) {
        VoucherDTO voucherDTO = voucherService.attachVoucherToUser(voucherId, request.getUserId());
        return ResponseEntity.ok(voucherDTO);
    }
}
