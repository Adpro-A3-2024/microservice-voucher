package com.adproa3.microservicevoucher.service;

import com.adproa3.microservicevoucher.model.*;
import com.adproa3.microservicevoucher.model.DTO.*;
import com.adproa3.microservicevoucher.repository.VoucherRepository;
import com.adproa3.microservicevoucher.repository.UserVoucherRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.time.*;
public interface VoucherService {
    public Voucher create(Voucher voucher);
    public List<Voucher> findAll();
    public VoucherResponseDTO useVoucher(VoucherUsageRequestDTO request);
    public List<VoucherDTO> getAllVouchers();
    public VoucherDTO createVoucher(VoucherDTO voucherDTO);
    public VoucherDTO updateVoucher(UUID id, VoucherDTO voucherDTO);
    public void deleteVoucher(UUID id);



}
