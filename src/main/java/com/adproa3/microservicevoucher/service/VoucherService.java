package com.adproa3.microservicevoucher.service;

import com.adproa3.microservicevoucher.model.Voucher;
import java.util.List;
public interface VoucherService {
    public Voucher create(Voucher voucher);
    public List<Voucher> findAll();
}
