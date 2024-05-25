package com.adproa3.microservicevoucher.service;

import com.adproa3.microservicevoucher.model.*;
import com.adproa3.microservicevoucher.model.DTO.*;

import java.util.List;
import java.util.UUID;

public interface VoucherReadService {
    public List<Voucher> findAll();
    public List<VoucherDTO> getAllVouchers();
    public List<VoucherDTO> getAllVouchersForUser(String userId);



}
