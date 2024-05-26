package com.adproa3.microservicevoucher.repository;

import com.adproa3.microservicevoucher.model.Voucher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, UUID> {
    List<Voucher> findByVoucherNameContainingIgnoreCase(String query);
    List<Voucher> findByAttachedUser(String userId);
}

