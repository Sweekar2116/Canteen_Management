package com.canteen.dto;

import com.canteen.entity.Payment;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateOrderRequest {

    private String couponCode;

    @NotNull(message = "Payment method is required")
    private Payment.PaymentMethod paymentMethod = Payment.PaymentMethod.CASH_ON_PICKUP;

    private LocalDateTime pickupTime;
    private String notes;

    public CreateOrderRequest() {}

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

    public Payment.PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(Payment.PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDateTime getPickupTime() { return pickupTime; }
    public void setPickupTime(LocalDateTime pickupTime) { this.pickupTime = pickupTime; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
