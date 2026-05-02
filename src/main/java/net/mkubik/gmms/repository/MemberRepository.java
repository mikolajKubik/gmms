package net.mkubik.gmms.repository;

import net.mkubik.gmms.model.Member;
import net.mkubik.gmms.model.MembershipStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
public interface MemberRepository extends JpaRepository<Member, UUID> {

    long countByMembershipPlanIdAndStatus(UUID membershipPlanId, MembershipStatus status); //TODO: unused

    @Query("""
            SELECT m FROM Member m
            JOIN FETCH m.membershipPlan mp
            JOIN FETCH mp.gym
            """)
    List<Member> findAllWithPlanAndGym();  // TODO: unused

    @Query(
            value = """
                SELECT m FROM Member m
                JOIN FETCH m.membershipPlan mp
                JOIN FETCH mp.gym
                """,
            countQuery = """
                SELECT count(m) FROM Member m
                """
    )
    Page<Member> findAllWithPlanAndGym(Pageable pageable);
}