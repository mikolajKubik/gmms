package net.mkubik.gmms.repository;

import net.mkubik.gmms.api.model.GymEntry;
import net.mkubik.gmms.model.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
public interface GymRepository extends JpaRepository<Gym, UUID> { //TODO: evaluate repo type

    // DTO projections
    @Query("""
            SELECT new net.mkubik.gmms.api.model.GymEntry(
                g.id,
                g.name,
                g.address,
                g.phoneNumber
            )
            FROM Gym g
            """)
    List<GymEntry> findAllGymEntries(); // TODO: pagination

    @Query("SELECT COUNT(g) > 0 FROM Gym g WHERE UPPER(g.name) = UPPER(:name)")
    boolean existsByNameIgnoreCase(@Param("name") String name);

    Gym save(Gym gym);
}

