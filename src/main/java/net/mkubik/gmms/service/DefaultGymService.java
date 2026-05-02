package net.mkubik.gmms.service;

import lombok.RequiredArgsConstructor;
import net.mkubik.gmms.exception.ResourceAlreadyExistsException;
import net.mkubik.gmms.model.Gym;
import net.mkubik.gmms.repository.GymRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultGymService implements GymService {

    private final GymRepository gymRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Gym createGym(Gym gym) {
        if (gymRepository.existsByNameIgnoreCase(gym.getName())) {
            throw new ResourceAlreadyExistsException(
                    "Gym with name '" + gym.getName() + "' already exists" // TODO: change to string consts
            );
        }
        try {
            return gymRepository.save(gym);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExistsException(
                    "Gym with name '" + gym.getName() + "' already exists"
            );
        }
    }
}
