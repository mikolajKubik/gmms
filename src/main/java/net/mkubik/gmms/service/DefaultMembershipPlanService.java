package net.mkubik.gmms.service;

import lombok.RequiredArgsConstructor;
import net.mkubik.gmms.exception.ResourceNotFoundException;
import net.mkubik.gmms.model.Gym;
import net.mkubik.gmms.model.MembershipPlan;
import net.mkubik.gmms.model.PlanType;
import net.mkubik.gmms.repository.GymRepository;
import net.mkubik.gmms.repository.MembershipPlanRepository;
import net.mkubik.gmms.repository.PlanTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultMembershipPlanService implements MembershipPlanService {

    private final MembershipPlanRepository membershipPlanRepository;
    private final GymRepository gymRepository;
    private final PlanTypeRepository planTypeRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public MembershipPlan createMembershipPlan(UUID gymId, MembershipPlan plan, String planTypeCode) {
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Gym with id '" + gymId + "' not found"
                ));

        PlanType planType = planTypeRepository.findByCode(planTypeCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Plan type with code '" + planTypeCode + "' not found"
                ));

        plan.setGym(gym);
        plan.setPlanType(planType);
        plan.setActiveMembers(0);

        return membershipPlanRepository.save(plan);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Page<MembershipPlan> listMembershipPlans(UUID gymId, Pageable pageable) {
        if (!gymRepository.existsById(gymId)) {
            throw new ResourceNotFoundException(
                    "Gym with id '" + gymId + "' not found"
            );
        }

        return membershipPlanRepository.findAllByGymId(gymId, pageable);
    }
}
