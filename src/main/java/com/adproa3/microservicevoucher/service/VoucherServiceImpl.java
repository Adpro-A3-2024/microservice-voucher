package com.adproa3.microservicevoucher.service;

import com.adproa3.microservicevoucher.model.Voucher;
import com.adproa3.microservicevoucher.repository.VoucherRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Service
public class VoucherServiceImpl implements VoucherService{

    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    public Voucher create(Voucher voucher){
        voucherRepository.create(voucher);
        return voucher;
    }
    @Override
    public List<Voucher> findAll(){
        Iterator<Voucher> voucherIterator = voucherRepository.findAll();
        List<Voucher> allVoucher = new ArrayList<>();
        voucherIterator.forEachRemaining(allVoucher::add);
        return allVoucher;
    }
}
