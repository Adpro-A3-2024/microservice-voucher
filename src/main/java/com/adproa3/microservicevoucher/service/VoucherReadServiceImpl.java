package com.adproa3.microservicevoucher.service;

import com.adproa3.microservicevoucher.model.Voucher;
import com.adproa3.microservicevoucher.model.*;
import com.adproa3.microservicevoucher.model.DTO.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import com.adproa3.microservicevoucher.repository.VoucherRepository;
import com.adproa3.microservicevoucher.repository.UserVoucherRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class VoucherReadServiceImpl implements VoucherReadService {
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private UserVoucherRepository userVoucherRepository;
    @Override
    public List<Voucher> findAll(){
        return voucherRepository.findAll();
    }

    public List<VoucherDTO> getAllVouchers() {
        List<Voucher> vouchers = voucherRepository.findAll();
        return vouchers.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public List<VoucherDTO> getAllVouchersForUser(String userId) {
        List<UserVoucher> userVouchers = userVoucherRepository.findByUserId(userId);
        return userVouchers.stream()
                .map(userVoucher -> convertToDto(userVoucher.getVoucher()))
                .collect(Collectors.toList());
    }
    private VoucherDTO convertToDto(Voucher voucher) {
        VoucherDTO voucherDTO = new VoucherDTO();
        BeanUtils.copyProperties(voucher, voucherDTO);
        return voucherDTO;
    }
}
