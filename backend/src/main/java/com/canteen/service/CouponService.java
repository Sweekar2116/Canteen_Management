package com.canteen.service;

import com.canteen.dto.CouponRequest;
import com.canteen.dto.CouponResponse;
import com.canteen.dto.ValidateCouponRequest;
import com.canteen.entity.Coupon;
import com.canteen.exception.BadRequestException;
import com.canteen.exception.ResourceNotFoundException;
import com.canteen.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final AuditLogService auditLogService;

    public CouponService(CouponRepository couponRepository, AuditLogService auditLogService) {
        this.couponRepository = couponRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional(readOnly = true)
    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CouponResponse validateCoupon(ValidateCouponRequest request) {
        Coupon coupon = couponRepository.findByCodeIgnoreCaseAndActiveTrue(request.getCode())
            .orElseThrow(() -> new BadRequestException("Invalid or inactive coupon code: " + request.getCode()));

        if (coupon.getExpiryDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Coupon '" + coupon.getCode() + "' has expired");
        }

        if (coupon.getUsageLimit() != null && coupon.getUsedCount() >= coupon.getUsageLimit()) {
            throw new BadRequestException("Coupon usage limit reached");
        }

        if (coupon.getMinOrderAmount() != null && request.getOrderAmount().compareTo(coupon.getMinOrderAmount()) < 0) {
            throw new BadRequestException("Minimum order amount of ₹" + coupon.getMinOrderAmount() + " required to use this coupon");
        }

        return mapToResponse(coupon);
    }

    public BigDecimal calculateDiscount(Coupon coupon, BigDecimal orderAmount) {
        BigDecimal discount = orderAmount.multiply(coupon.getDiscountPercent())
            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        if (coupon.getMaxDiscount() != null && discount.compareTo(coupon.getMaxDiscount()) > 0) {
            discount = coupon.getMaxDiscount();
        }

        return discount;
    }

    @Transactional
    public CouponResponse createCoupon(Long adminId, CouponRequest request) {
        if (couponRepository.findByCodeIgnoreCase(request.getCode()).isPresent()) {
            throw new BadRequestException("Coupon code already exists: " + request.getCode());
        }

        Coupon coupon = new Coupon();
        coupon.setCode(request.getCode().toUpperCase());
        coupon.setDescription(request.getDescription());
        coupon.setDiscountPercent(request.getDiscountPercent());
        coupon.setMaxDiscount(request.getMaxDiscount());
        coupon.setMinOrderAmount(request.getMinOrderAmount() != null ? request.getMinOrderAmount() : BigDecimal.ZERO);
        coupon.setExpiryDate(request.getExpiryDate());
        coupon.setUsageLimit(request.getUsageLimit());
        coupon.setActive(request.isActive());

        Coupon saved = couponRepository.save(coupon);
        auditLogService.log(adminId, "CREATE_COUPON", "Coupon", saved.getId(), "Created coupon: " + saved.getCode());
        return mapToResponse(saved);
    }

    @Transactional
    public CouponResponse updateCoupon(Long adminId, Long id, CouponRequest request) {
        Coupon coupon = couponRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));

        coupon.setDescription(request.getDescription());
        coupon.setDiscountPercent(request.getDiscountPercent());
        coupon.setMaxDiscount(request.getMaxDiscount());
        coupon.setMinOrderAmount(request.getMinOrderAmount());
        coupon.setExpiryDate(request.getExpiryDate());
        coupon.setUsageLimit(request.getUsageLimit());
        coupon.setActive(request.isActive());

        Coupon updated = couponRepository.save(coupon);
        auditLogService.log(adminId, "UPDATE_COUPON", "Coupon", updated.getId(), "Updated coupon: " + updated.getCode());
        return mapToResponse(updated);
    }

    @Transactional
    public void toggleCouponStatus(Long adminId, Long id) {
        Coupon coupon = couponRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));
        coupon.setActive(!coupon.isActive());
        couponRepository.save(coupon);

        auditLogService.log(adminId, "TOGGLE_COUPON_STATUS", "Coupon", id, "Coupon active status set to: " + coupon.isActive());
    }

    private CouponResponse mapToResponse(Coupon coupon) {
        CouponResponse res = new CouponResponse();
        res.setId(coupon.getId());
        res.setCode(coupon.getCode());
        res.setDescription(coupon.getDescription());
        res.setDiscountPercent(coupon.getDiscountPercent());
        res.setMaxDiscount(coupon.getMaxDiscount());
        res.setMinOrderAmount(coupon.getMinOrderAmount());
        res.setExpiryDate(coupon.getExpiryDate());
        res.setUsageLimit(coupon.getUsageLimit());
        res.setUsedCount(coupon.getUsedCount());
        res.setActive(coupon.isActive());
        return res;
    }
}
