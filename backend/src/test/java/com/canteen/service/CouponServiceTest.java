package com.canteen.service;

import com.canteen.dto.CouponResponse;
import com.canteen.dto.ValidateCouponRequest;
import com.canteen.entity.Coupon;
import com.canteen.exception.BadRequestException;
import com.canteen.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private CouponService couponService;

    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = new Coupon();
        coupon.setId(1L);
        coupon.setCode("SAVE20");
        coupon.setDiscountPercent(new BigDecimal("20.00"));
        coupon.setMaxDiscount(new BigDecimal("50.00"));
        coupon.setMinOrderAmount(new BigDecimal("100.00"));
        coupon.setExpiryDate(LocalDate.now().plusMonths(1));
        coupon.setActive(true);
        coupon.setUsedCount(0);
        coupon.setUsageLimit(100);
    }

    @Test
    void testValidateCoupon_Success() {
        when(couponRepository.findByCodeIgnoreCaseAndActiveTrue("SAVE20")).thenReturn(Optional.of(coupon));

        ValidateCouponRequest request = new ValidateCouponRequest("SAVE20", new BigDecimal("200.00"));
        CouponResponse response = couponService.validateCoupon(request);

        assertNotNull(response);
        assertEquals("SAVE20", response.getCode());
        assertEquals(new BigDecimal("20.00"), response.getDiscountPercent());
    }

    @Test
    void testValidateCoupon_BelowMinAmount_ThrowsBadRequest() {
        when(couponRepository.findByCodeIgnoreCaseAndActiveTrue("SAVE20")).thenReturn(Optional.of(coupon));

        ValidateCouponRequest request = new ValidateCouponRequest("SAVE20", new BigDecimal("50.00"));

        assertThrows(BadRequestException.class, () -> couponService.validateCoupon(request));
    }

    @Test
    void testCalculateDiscount_CapsAtMaxDiscount() {
        // 20% of 400 is 80, but maxDiscount is 50
        BigDecimal discount = couponService.calculateDiscount(coupon, new BigDecimal("400.00"));
        assertEquals(new BigDecimal("50.00"), discount);
    }
}
