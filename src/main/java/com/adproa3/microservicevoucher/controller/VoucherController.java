package com.adproa3.microservicevoucher.controller;

import org.springframework.http.ResponseEntity;
import com.adproa3.microservicevoucher.model.*;
import com.adproa3.microservicevoucher.model.DTO.*;
import com.adproa3.microservicevoucher.service.VoucherService;
import com.adproa3.microservicevoucher.service.VoucherServiceImpl;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @PostMapping("/use")
    public ResponseEntity<VoucherResponseDTO> useVoucher(@RequestBody VoucherUsageRequestDTO request) {
        VoucherResponseDTO response = voucherService.useVoucher(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<VoucherDTO> getAllVouchers() {
        return voucherService.getAllVouchers();
    }

    @PostMapping
    public VoucherDTO createVoucher(@RequestBody VoucherDTO voucherDTO) {
        return voucherService.createVoucher(voucherDTO);
    }

    @PutMapping("/{id}")
    public VoucherDTO updateVoucher(@PathVariable UUID id, @RequestBody VoucherDTO voucherDTO) {
        return voucherService.updateVoucher(id, voucherDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteVoucher(@PathVariable UUID id) {
        voucherService.deleteVoucher(id);
    }
}
