package com.adproa3.microservicevoucher.service;

import com.adproa3.microservicevoucher.model.*;
import com.adproa3.microservicevoucher.model.DTO.*;
import com.adproa3.microservicevoucher.repository.VoucherRepository;
import com.adproa3.microservicevoucher.repository.UserVoucherRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface VoucherWriteService {
    public CompletableFuture<VoucherDTO> createVoucherAsync(VoucherDTO voucherDTO);
    public CompletableFuture<VoucherDTO> updateVoucherAsync(UUID id, VoucherDTO voucherDTO);
    public CompletableFuture<Void> deleteVoucherAsync(UUID id);

    Voucher create(Voucher voucher);

    public VoucherDTO createVoucher(VoucherDTO voucherDTO);
    public VoucherDTO updateVoucher(UUID id, VoucherDTO voucherDTO);
    public void deleteVoucher(UUID id);
}
