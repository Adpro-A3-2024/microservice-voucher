package com.adproa3.microservicevoucher.service;

import com.adproa3.microservicevoucher.model.*;
import com.adproa3.microservicevoucher.model.DTO.*;
import com.adproa3.microservicevoucher.repository.VoucherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanUtils;

import java.util.concurrent.CompletableFuture;
import java.util.UUID;
import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VoucherWriteServiceImpl implements VoucherWriteService{

    @Autowired
    private VoucherRepository voucherRepository;
    @Async
    public CompletableFuture<VoucherDTO> createVoucherAsync(@Valid VoucherDTO voucherDTO) {
        return CompletableFuture.supplyAsync(() -> createVoucher(voucherDTO));
    }

    @Async
    public CompletableFuture<VoucherDTO> updateVoucherAsync(UUID id, @Valid VoucherDTO voucherDTO) {
        return CompletableFuture.supplyAsync(() -> updateVoucher(id, voucherDTO));
    }

    @Async
    public CompletableFuture<Void> deleteVoucherAsync(UUID id) {
        return CompletableFuture.runAsync(() -> deleteVoucher(id));
    }

    @Override
    public Voucher create (Voucher voucher){
        voucherRepository.save(voucher);
        return voucher;
    }

    public VoucherDTO createVoucher(@Valid VoucherDTO voucherDTO) {
        validateVoucher(voucherDTO);
        Voucher voucher = new Voucher();
        BeanUtils.copyProperties(voucherDTO, voucher, "voucherId");
        voucher = voucherRepository.save(voucher);
        return convertToDto(voucher);
    }

    public VoucherDTO updateVoucher(UUID id, @Valid VoucherDTO voucherDTO) {
        validateVoucher(voucherDTO);
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voucher not found"));
        BeanUtils.copyProperties(voucherDTO, voucher, "voucherId");
        voucher = voucherRepository.save(voucher);
        return convertToDto(voucher); // Salin ID yang dihasilkan dari entitas ke DTO
    }

    public void deleteVoucher(UUID id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voucher not found"));
        voucherRepository.delete(voucher);
    }

    private void validateVoucher(VoucherDTO voucherDTO) {
        if (voucherDTO.getVoucherName() == null || voucherDTO.getVoucherName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Voucher name is required");
        }
        if (voucherDTO.getVoucherAmount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Voucher amount must be greater than zero");
        }
        if (voucherDTO.getRequiredSpending() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Required spending must be greater than zero");
        }
    }

    private VoucherDTO convertToDto(Voucher voucher) {
        VoucherDTO voucherDTO = new VoucherDTO();
        BeanUtils.copyProperties(voucher, voucherDTO);
        return voucherDTO;
    }

}
