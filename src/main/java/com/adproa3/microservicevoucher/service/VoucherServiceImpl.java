package com.adproa3.microservicevoucher.service;

import com.adproa3.microservicevoucher.model.*;
import com.adproa3.microservicevoucher.model.DTO.*;
import com.adproa3.microservicevoucher.repository.VoucherRepository;
import com.adproa3.microservicevoucher.repository.UserVoucherRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;
import java.util.UUID;
import java.time.*;
import jakarta.validation.Valid;
@Service
public class VoucherServiceImpl implements VoucherService{

    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private UserVoucherRepository userVoucherRepository;

    @Override
    public Voucher create(Voucher voucher){
        voucherRepository.save(voucher);
        return voucher;
    }
    @Override
    public List<Voucher> findAll(){
        return voucherRepository.findAll();
    }

    public VoucherResponseDTO useVoucher(VoucherUsageRequestDTO request) {
        Voucher voucher = voucherRepository.findById(request.getVoucherId())
                .orElseThrow(() -> new RuntimeException("Voucher not found"));

        LocalDate today = LocalDate.now();

        if (today.isBefore(voucher.getStartDate()) || today.isAfter(voucher.getEndDate())) {
            return new VoucherResponseDTO(false, "Voucher tidak berlaku pada tanggal ini", request.getCartTotal(), null);
        }

        if (voucher.getQuota() > voucher.getUsed()) {
            if (request.getCartTotal() >= voucher.getRequiredSpending()) {
                voucher.setUsed(voucher.getUsed() + 1);
                voucherRepository.save(voucher);

                UserVoucher userVoucher = new UserVoucher();
                userVoucher.setUserId(request.getUserId());
                userVoucher.setVoucher(voucher);
                userVoucher.setUsedAt(LocalDateTime.now());
                userVoucherRepository.save(userVoucher);

                double discountedTotal = request.getCartTotal() * (1 - voucher.getVoucherAmount() / 100);

                VoucherDTO voucherDTO = new VoucherDTO();
                BeanUtils.copyProperties(voucher, voucherDTO);

                return new VoucherResponseDTO(true, "Voucher berhasil digunakan", discountedTotal, voucherDTO);
            } else {
                return new VoucherResponseDTO(false, "Gagal menggunakan voucher. Total belanja kurang dari syarat minimum.", request.getCartTotal(), null);
            }
        } else {
            return new VoucherResponseDTO(false, "Gagal menggunakan voucher. Kuota habis.", request.getCartTotal(), null);
        }
    }
    public List<VoucherDTO> getAllVouchers() {
        List<Voucher> vouchers = voucherRepository.findAll();
        return vouchers.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public VoucherDTO createVoucher(@Valid VoucherDTO voucherDTO) {
        validateVoucher(voucherDTO);
        Voucher voucher = new Voucher();
        BeanUtils.copyProperties(voucherDTO, voucher, "voucherId"); // Jangan menyalin ID
        voucher = voucherRepository.save(voucher);
        return convertToDto(voucher); // Salin ID yang dihasilkan dari entitas ke DTO
    }

    public VoucherDTO updateVoucher(UUID id, @Valid VoucherDTO voucherDTO) {
        validateVoucher(voucherDTO);
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));
        BeanUtils.copyProperties(voucherDTO, voucher, "voucherId"); // Jangan menyalin ID
        voucher = voucherRepository.save(voucher);
        return convertToDto(voucher); // Salin ID yang dihasilkan dari entitas ke DTO
    }

    public void deleteVoucher(UUID id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));
        voucherRepository.delete(voucher);
    }

    private void validateVoucher(VoucherDTO voucherDTO) {
        if (voucherDTO.getVoucherName() == null || voucherDTO.getVoucherName().isEmpty()) {
            throw new IllegalArgumentException("Voucher name is required");
        }
        if (voucherDTO.getVoucherAmount() <= 0) {
            throw new IllegalArgumentException("Discount amount must be greater than zero");
        }
    }

    private VoucherDTO convertToDto(Voucher voucher) {
        VoucherDTO voucherDTO = new VoucherDTO();
        BeanUtils.copyProperties(voucher, voucherDTO);
        return voucherDTO;
    }

}
