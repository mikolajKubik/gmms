package net.mkubik.gmms.service;

import lombok.RequiredArgsConstructor;
import net.mkubik.gmms.model.Gym;
import net.mkubik.gmms.repository.GymRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class GymServiceImpl implements GymService {

    private final GymRepository gymRepository;

    @Override
    @Transactional
    public Gym createGym(Gym gym) { //TODO: refactor
        if (gymRepository.existsByNameIgnoreCase(gym.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Gym with name '" + gym.getName() + "' already exists"
            );
        }
        try {
            return gymRepository.save(gym);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Gym with name '" + gym.getName() + "' already exists",
                    e
            );
        }
    }
}
