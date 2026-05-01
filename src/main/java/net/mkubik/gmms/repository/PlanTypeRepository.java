package net.mkubik.gmms.repository;

import net.mkubik.gmms.model.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(propagation = Propagation.MANDATORY)
public interface PlanTypeRepository extends JpaRepository<PlanType, Long> {

    Optional<PlanType> findByCode(String code);
}
