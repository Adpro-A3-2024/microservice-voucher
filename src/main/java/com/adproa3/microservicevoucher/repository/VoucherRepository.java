package com.adproa3.microservicevoucher.repository;

import com.adproa3.microservicevoucher.model.Voucher;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class VoucherRepository {
    private List<Voucher> voucherData = new ArrayList<>();

    public Voucher create(Voucher voucher){
        voucherData.add(voucher);
        return voucher;
    }

    public Iterator<Voucher> findAll() {
        return voucherData.iterator();
    }
}
