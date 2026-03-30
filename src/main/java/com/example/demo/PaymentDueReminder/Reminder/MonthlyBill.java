package com.example.demo.PaymentDueReminder.Reminder;

import com.example.demo.Enums.NormalUserType;
import com.example.demo.Enums.PaymentStatus;
import com.example.demo.Enums.UserRole;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "monthly_bills")
public class MonthlyBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Society & User Info
    private Integer societyId;
    private String societyName;
    private Integer userId;      // tenant / owner
    private String userName;
    private Integer flatId;      // optional (future)

    // Billing Info
    private String billMonth;    // e.g. "2026-02"
    private Double totalAmount;
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;          // UNPAID, PAID, PARTIAL

    //  Audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //  Relationship
    @OneToMany(
            mappedBy = "monthlyBill",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    private List<BillItem> items;

    //  Auto timestamps
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    private Double paidAmount;
    private Double dueAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole createdByRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "receiver_role")
    private NormalUserType receiverRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "last_updated_by_role")
    private UserRole lastUpdatedByRole;

    @Column(name = "last_updated_by_user_id")
    private Integer lastUpdatedByUserId;

    @Column(name = "status_updated_at")
    private LocalDateTime statusUpdatedAt;


// GETTERS & SETTERS


    public UserRole getLastUpdatedByRole() {
        return lastUpdatedByRole;
    }

    public void setLastUpdatedByRole(UserRole lastUpdatedByRole) {
        this.lastUpdatedByRole = lastUpdatedByRole;
    }

    public Integer getLastUpdatedByUserId() {
        return lastUpdatedByUserId;
    }

    public void setLastUpdatedByUserId(Integer lastUpdatedByUserId) {
        this.lastUpdatedByUserId = lastUpdatedByUserId;
    }

    public LocalDateTime getStatusUpdatedAt() {
        return statusUpdatedAt;
    }

    public void setStatusUpdatedAt(LocalDateTime statusUpdatedAt) {
        this.statusUpdatedAt = statusUpdatedAt;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSocietyName() {
        return societyName;
    }

    public void setSocietyName(String societyName) {
        this.societyName = societyName;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFlatId() {
        return flatId;
    }

    public void setFlatId(Integer flatId) {
        this.flatId = flatId;
    }

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<BillItem> getItems() {
        return items;
    }

    public void setItems(List<BillItem> items) {
        this.items = items;
    }
}
