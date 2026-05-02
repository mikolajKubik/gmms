package net.mkubik.gmms.service;

import lombok.RequiredArgsConstructor;
import net.mkubik.gmms.exception.ResourceAlreadyCancelledException;
import net.mkubik.gmms.exception.ResourceCapacityExceededException;
import net.mkubik.gmms.exception.ResourceNotFoundException;
import net.mkubik.gmms.model.Member;
import net.mkubik.gmms.model.MembershipPlan;
import net.mkubik.gmms.model.MembershipStatus;
import net.mkubik.gmms.repository.MemberRepository;
import net.mkubik.gmms.repository.MembershipPlanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

// TODO: @Transactional(propagation = Propagation.REQUIRES_NEW
@Service
@RequiredArgsConstructor
public class DefaultMemberService implements MemberService {
    private final MemberRepository memberRepository;
    private final MembershipPlanRepository membershipPlanRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Member registerMember(UUID planId, Member member) {
        MembershipPlan plan = membershipPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Membership plan with id '" + planId + "' not found"
                ));

        if (plan.getActiveMembers() >= plan.getMaxMembers()) {
            throw new ResourceCapacityExceededException(
                    "Membership plan '" + plan.getName() + "' has reached maximum capacity"
            );
        }

        plan.setActiveMembers(plan.getActiveMembers() + 1);

        member.setMembershipPlan(plan);
        member.setStartDate(LocalDate.now());
        member.setStatus(MembershipStatus.ACTIVE);

        membershipPlanRepository.saveAndFlush(plan);
        return memberRepository.saveAndFlush(member);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Page<Member> listMembers(Pageable pageable) {
        return memberRepository.findAllWithPlanAndGym(pageable);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Member cancelMembership(UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Member with id '" + memberId + "' not found"
                ));

        if (member.getStatus() == MembershipStatus.CANCELLED) {
            throw new ResourceAlreadyCancelledException(
                    "Membership for member '" + memberId + "' is already cancelled"
            );
        }

        member.setStatus(MembershipStatus.CANCELLED);

        MembershipPlan plan = member.getMembershipPlan();
        plan.setActiveMembers(plan.getActiveMembers() - 1);

        membershipPlanRepository.saveAndFlush(plan);
        return memberRepository.saveAndFlush(member);
    }
}
