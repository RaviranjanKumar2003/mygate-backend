package com.example.demo.PaymentDueReminder.Reminder;

import com.example.demo.Enums.NormalUserType;
import com.example.demo.Enums.PaymentStatus;
import com.example.demo.Enums.UserRole;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MonthlyBillDto {

    private Integer id;

    private Integer societyId;
    private String societyName;

    private Integer userId;          // Tenant / Owner
    private String userName;
    private Integer flatId;

    private String billMonth;     // 2026-02

    private Double totalAmount;
    private Double paidAmount;
    private Double dueAmount;

    private PaymentStatus status;    // PENDING, COMPLETED, FAILED, REFUNDED

    private LocalDateTime billCreatedAt;
    private LocalDate dueDate;
    private LocalDateTime updatedAt;

    // Optional (for details screen)
    private List<BillItemDto> items;

    private UserRole createdByRole;

    private NormalUserType receiverRole;

// GETTERS & SETTERS


    public UserRole getCreatedByRole() {
        return createdByRole;
    }

    public void setCreatedByRole(UserRole createdByRole) {
        this.createdByRole = createdByRole;
    }

    public NormalUserType getReceiverRole() {
        return receiverRole;
    }

    public void setReceiverRole(NormalUserType receiverRole) {
        this.receiverRole = receiverRole;
    }

    public Integer getFlatId() {
        return flatId;
    }

    public void setFlatId(Integer flatId) {
        this.flatId = flatId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Integer societyId) {
        this.societyId = societyId;
    }

    public String getSocietyName() {
        return societyName;
    }

    public void setSocietyName(String societyName) {
        this.societyName = societyName;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Double getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(Double dueAmount) {
        this.dueAmount = dueAmount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getBillCreatedAt() {
        return billCreatedAt;
    }

    public void setBillCreatedAt(LocalDateTime billCreatedAt) {
        this.billCreatedAt = billCreatedAt;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<BillItemDto> getItems() {
        return items;
    }

    public void setItems(List<BillItemDto> items) {
        this.items = items;
    }
}
