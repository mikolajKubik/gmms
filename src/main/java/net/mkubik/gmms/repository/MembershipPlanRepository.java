package net.mkubik.gmms.repository;

import net.mkubik.gmms.model.MembershipPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, UUID> {

    Page<MembershipPlan> findAllByGymId(UUID gymId, Pageable pageable);
}
