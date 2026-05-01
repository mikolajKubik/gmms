package net.mkubik.gmms.repository;

import net.mkubik.gmms.api.model.GymEntry;
import net.mkubik.gmms.model.Gym;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional(propagation = Propagation.MANDATORY)
public interface GymRepository extends Repository<Gym, UUID> {

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
    List<GymEntry> findAllGymEntries();
}

