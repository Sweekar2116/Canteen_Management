package com.canteen.controller;

import com.canteen.dto.CouponRequest;
import com.canteen.dto.CouponResponse;
import com.canteen.security.UserPrincipal;
import com.canteen.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/coupons")
@Tag(name = "Admin Coupon Management", description = "Coupon creation, update and deactivation APIs")
public class AdminCouponController {

    private final CouponService couponService;

    public AdminCouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    @Operation(summary = "Get all coupons")
    public ResponseEntity<List<CouponResponse>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @PostMapping
    @Operation(summary = "Create a new discount coupon")
    public ResponseEntity<CouponResponse> createCoupon(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @Valid @RequestBody CouponRequest request
    ) {
        return new ResponseEntity<>(couponService.createCoupon(currentUser.getId(), request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing coupon")
    public ResponseEntity<CouponResponse> updateCoupon(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @PathVariable Long id,
        @Valid @RequestBody CouponRequest request
    ) {
        return ResponseEntity.ok(couponService.updateCoupon(currentUser.getId(), id, request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Toggle active/inactive status of coupon")
    public ResponseEntity<Void> toggleStatus(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @PathVariable Long id
    ) {
        couponService.toggleCouponStatus(currentUser.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
