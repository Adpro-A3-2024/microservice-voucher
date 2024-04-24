package com.adproa3.microservicevoucher.controller;

import com.adproa3.microservicevoucher.model.Voucher;
import com.adproa3.microservicevoucher.service.VoucherService;
import com.adproa3.microservicevoucher.service.VoucherServiceImpl;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/voucher")
public class VoucherController {
    @Autowired
    private VoucherService service;

    @GetMapping("/create")
    public String createVoucherPage(Model model){
        Voucher voucher = new Voucher();
        model.addAttribute("voucher",voucher);
        return "createVoucher";
    }

    @PostMapping("/create")
    public String createVoucherPost(@ModelAttribute Voucher voucher, Model model){
        service.create(voucher);
        return "redirect:list";
    }

    @GetMapping("/list")
    public String voucherListPage(Model model){
        List<Voucher> allVoucher = service.findAll();
        model.addAttribute("vouchers", allVoucher);
        return "voucherList";
    }
}
