package com.adproa3.microservicevoucher.service;
import com.adproa3.microservicevoucher.model.*;
import com.adproa3.microservicevoucher.model.DTO.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.adproa3.microservicevoucher.repository.VoucherRepository;
import com.adproa3.microservicevoucher.repository.UserVoucherRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private UserVoucherRepository userVoucherRepository;
    @Autowired
    private UserService userService;

    @Async
    public CompletableFuture<VoucherResponseDTO> useVoucherAsync(VoucherUsageRequestDTO request) {
        return CompletableFuture.supplyAsync(() -> {
            return useVoucher(request);
        });
    }
    @Async
    public CompletableFuture<VoucherDTO> attachVoucherToUserAsync(UUID voucherId, String userId) {
        return CompletableFuture.supplyAsync(() -> {
            return attachVoucherToUser(voucherId, userId);
        });
    }
    public VoucherDTO attachVoucherToUser(UUID voucherId, String userId) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voucher not found"));

        voucher.setAttachedUser(userId);
        voucherRepository.save(voucher);

        return convertToDto(voucher);
    }

    public VoucherResponseDTO useVoucher(VoucherUsageRequestDTO request) {
//        Mono<UserDTO> userMono = userService.getUserById(request.getUserId());
//        UserDTO user = userMono.block();
//        if (request.getUserId() == null) {
//            throw new RuntimeException("User not found");
//        }
        Optional<Voucher> optionalVoucher = voucherRepository.findById(request.getVoucherId());
        if (optionalVoucher.isEmpty()) {
            return new VoucherResponseDTO(false, "Voucher tidak ditemukan", request.getCartTotal(), null);
        }
        Voucher voucher = optionalVoucher.get();
        if (voucher.getAttachedUser() == null){
            return new VoucherResponseDTO(false, "Voucher belum Di attach ke user", request.getCartTotal(), null);
        }
        if (!voucher.getAttachedUser().equals(request.getUserId())) {
            return new VoucherResponseDTO(false, "User tidak ditemukan", request.getCartTotal(), null);
        }

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

    private VoucherDTO convertToDto(Voucher voucher) {
        VoucherDTO voucherDTO = new VoucherDTO();
        BeanUtils.copyProperties(voucher, voucherDTO);
        return voucherDTO;
    }

}
