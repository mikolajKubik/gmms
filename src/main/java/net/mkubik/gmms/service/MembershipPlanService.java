package net.mkubik.gmms.service;

import net.mkubik.gmms.model.MembershipPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MembershipPlanService {

    MembershipPlan createMembershipPlan(UUID gymId, MembershipPlan plan, String planTypeCode);

    Page<MembershipPlan> listMembershipPlans(UUID gymId, Pageable pageable);
}
