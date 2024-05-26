package com.adproa3.microservicevoucher;
import com.adproa3.microservicevoucher.model.DTO.*;
import com.adproa3.microservicevoucher.model.*;
import com.adproa3.microservicevoucher.repository.*;
import com.adproa3.microservicevoucher.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VoucherServiceTest {

    @Mock
    private VoucherRepository voucherRepository;
    @Mock
    private UserVoucherRepository userVoucherRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private VoucherServiceImpl voucherService;

    @InjectMocks
    private VoucherReadServiceImpl voucherReadService;

    @InjectMocks
    private VoucherWriteServiceImpl voucherWriteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateVoucherSuccess() {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setVoucherName("Test Voucher");
        voucherDTO.setVoucherAmount(10);
        voucherDTO.setRequiredSpending(100);

        Voucher voucher = new Voucher();
        voucher.setVoucherId(UUID.randomUUID());
        voucher.setVoucherName("Test Voucher");
        voucher.setVoucherAmount(10);
        voucher.setRequiredSpending(100);

        when(voucherRepository.save(any(Voucher.class))).thenReturn(voucher);

        VoucherDTO createdVoucher = voucherWriteService.createVoucher(voucherDTO);

        assertNotNull(createdVoucher);
        assertEquals("Test Voucher", createdVoucher.getVoucherName());
        verify(voucherRepository, times(1)).save(any(Voucher.class));
    }

    @Test
    void testCreateVoucherInvalidName() {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setVoucherAmount(10);
        voucherDTO.setRequiredSpending(100);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            voucherWriteService.createVoucher(voucherDTO);
        });

        assertEquals("Voucher name is required", exception.getReason());
        assertEquals(400, exception.getStatusCode().value());
        verify(voucherRepository, never()).save(any(Voucher.class));
    }

    @Test
    void testCreateVoucherInvalidAmount() {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setVoucherName("Test Voucher");
        voucherDTO.setVoucherAmount(0);
        voucherDTO.setRequiredSpending(100);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            voucherWriteService.createVoucher(voucherDTO);
        });

        assertEquals("Voucher amount must be greater than zero", exception.getReason());
        assertEquals(400, exception.getStatusCode().value());
        verify(voucherRepository, never()).save(any(Voucher.class));
    }

    @Test
    void testCreateVoucherInvalidRequiredSpending() {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setVoucherName("Test Voucher");
        voucherDTO.setVoucherAmount(10);
        voucherDTO.setRequiredSpending(-1);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            voucherWriteService.createVoucher(voucherDTO);
        });

        assertEquals("Required spending must be greater than zero", exception.getReason());
        assertEquals(400, exception.getStatusCode().value());
        verify(voucherRepository, never()).save(any(Voucher.class));
    }

    @Test
    void testUseVoucherInsufficientSpending() {
        UUID voucherId = UUID.randomUUID();
        String userId = "user123";
        Voucher voucher = new Voucher();
        voucher.setVoucherId(voucherId);
        voucher.setVoucherName("Test Voucher");
        voucher.setVoucherAmount(10);
        voucher.setRequiredSpending(100);
        voucher.setStartDate(LocalDate.now().minusDays(1));
        voucher.setEndDate(LocalDate.now().plusDays(1));
        voucher.setQuota(10);
        voucher.setUsed(5);
        voucher.setAttachedUser(userId);

        VoucherUsageRequestDTO requestDTO = new VoucherUsageRequestDTO();
        requestDTO.setUserId(userId);
        requestDTO.setVoucherId(voucherId);
        requestDTO.setCartTotal(50);

        when(voucherRepository.findById(voucherId)).thenReturn(Optional.of(voucher));

        VoucherResponseDTO response = voucherService.useVoucher(requestDTO);

        assertFalse(response.isSuccess());
        assertEquals(50, response.getTotalAfterDiscount());
        assertEquals("Gagal menggunakan voucher. Total belanja kurang dari syarat minimum.", response.getMessage());
        verify(voucherRepository, never()).save(any(Voucher.class));
    }

    @Test
    void testUseVoucherExpired() {
        UUID voucherId = UUID.randomUUID();
        String userId = "user123";
        Voucher voucher = new Voucher();
        voucher.setVoucherId(voucherId);
        voucher.setVoucherName("Test Voucher");
        voucher.setVoucherAmount(10);
        voucher.setRequiredSpending(100);
        voucher.setStartDate(LocalDate.now().minusDays(10));
        voucher.setEndDate(LocalDate.now().minusDays(5));
        voucher.setQuota(10);
        voucher.setUsed(5);
        voucher.setAttachedUser(userId);

        VoucherUsageRequestDTO requestDTO = new VoucherUsageRequestDTO();
        requestDTO.setUserId(userId);
        requestDTO.setVoucherId(voucherId);
        requestDTO.setCartTotal(200);

        when(voucherRepository.findById(voucherId)).thenReturn(Optional.of(voucher));

        VoucherResponseDTO response = voucherService.useVoucher(requestDTO);

        assertFalse(response.isSuccess());
        assertEquals(200, response.getTotalAfterDiscount());
        assertEquals("Voucher tidak berlaku pada tanggal ini", response.getMessage());
        verify(voucherRepository, never()).save(any(Voucher.class));
    }

    @Test
    void testUseVoucherQuotaExhausted() {
        UUID voucherId = UUID.randomUUID();
        String userId = "user123";
        Voucher voucher = new Voucher();
        voucher.setVoucherId(voucherId);
        voucher.setVoucherName("Test Voucher");
        voucher.setVoucherAmount(10);
        voucher.setRequiredSpending(100);
        voucher.setStartDate(LocalDate.now().minusDays(1));
        voucher.setEndDate(LocalDate.now().plusDays(1));
        voucher.setQuota(10);
        voucher.setUsed(10);
        voucher.setAttachedUser(userId);

        VoucherUsageRequestDTO requestDTO = new VoucherUsageRequestDTO();
        requestDTO.setUserId(userId);
        requestDTO.setVoucherId(voucherId);
        requestDTO.setCartTotal(200);

        when(voucherRepository.findById(voucherId)).thenReturn(Optional.of(voucher));

        VoucherResponseDTO response = voucherService.useVoucher(requestDTO);

        assertFalse(response.isSuccess());
        assertEquals(200, response.getTotalAfterDiscount());
        assertEquals("Gagal menggunakan voucher. Kuota habis.", response.getMessage());
        verify(voucherRepository, never()).save(any(Voucher.class));
    }

    @Test
    void testUseVoucherUserNotFound() {
        UUID voucherId = UUID.randomUUID();
        String userId = "user123";
        Voucher voucher = new Voucher();
        voucher.setVoucherId(voucherId);
        voucher.setVoucherName("Test Voucher");
        voucher.setVoucherAmount(10);
        voucher.setRequiredSpending(100);
        voucher.setStartDate(LocalDate.now().minusDays(1));
        voucher.setEndDate(LocalDate.now().plusDays(1));
        voucher.setQuota(10);
        voucher.setUsed(5);
        voucher.setAttachedUser("otherUser");

        VoucherUsageRequestDTO requestDTO = new VoucherUsageRequestDTO();
        requestDTO.setUserId(userId);
        requestDTO.setVoucherId(voucherId);
        requestDTO.setCartTotal(200);

        when(voucherRepository.findById(voucherId)).thenReturn(Optional.of(voucher));

        VoucherResponseDTO response = voucherService.useVoucher(requestDTO);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(200, response.getTotalAfterDiscount());
        assertEquals("User tidak ditemukan", response.getMessage());
        verify(voucherRepository, never()).save(any(Voucher.class));
    }

    @Test
    void testAttachVoucherToUserSuccess() {
        UUID voucherId = UUID.randomUUID();
        String userId = "user123";

        Voucher voucher = new Voucher();
        voucher.setVoucherId(voucherId);
        voucher.setVoucherName("Test Voucher");

        when(voucherRepository.findById(voucherId)).thenReturn(Optional.of(voucher));
        when(voucherRepository.save(any(Voucher.class))).thenReturn(voucher);

        VoucherDTO result = voucherService.attachVoucherToUser(voucherId, userId);

        assertNotNull(result);
        assertEquals(userId, voucher.getAttachedUser());
        verify(voucherRepository, times(1)).save(any(Voucher.class));
    }

    @Test
    void testAttachVoucherToUserNotFound() {
        UUID voucherId = UUID.randomUUID();
        String userId = "user123";

        when(voucherRepository.findById(voucherId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            voucherService.attachVoucherToUser(voucherId, userId);
        });

        assertEquals("Voucher not found", exception.getReason());
        assertEquals(404, exception.getStatusCode().value());
        verify(voucherRepository, never()).save(any(Voucher.class));
    }
}

