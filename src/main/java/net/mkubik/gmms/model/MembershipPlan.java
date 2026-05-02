package net.mkubik.gmms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "membership_plan")
@AllArgsConstructor
@NoArgsConstructor
public class MembershipPlan extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {})
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = {})
    @JoinColumn(name = "plan_type_id", nullable = false)
    private PlanType planType;

    @NotBlank
    @Size(max = 255)
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @NotNull
    @Positive
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotBlank
    @Size(min = 3, max = 3)
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @NotNull
    @Min(1)
    @Column(name = "duration_months", nullable = false)
    private Integer durationMonths;

    @NotNull
    @Min(1)
    @Column(name = "max_members", nullable = false)
    private Integer maxMembers;

    @NotNull
    @Min(0)
    @Column(name = "active_members", nullable = false)
    private Integer activeMembers = 0;
}
