package net.mkubik.gmms.controller.api.v1;

import lombok.RequiredArgsConstructor;
import net.mkubik.gmms.api.DefaultApi;
import net.mkubik.gmms.api.model.CreateGymRequest;
import net.mkubik.gmms.api.model.CreateGymResponse;
import net.mkubik.gmms.api.model.ListGymsResponse;
import net.mkubik.gmms.repository.GymRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GymController implements DefaultApi {

    private final GymRepository gymRepository;

    @Override
    public ResponseEntity<CreateGymResponse> createGym(CreateGymRequest createGymRequest) {
        // TODO:
        CreateGymResponse response = new CreateGymResponse();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ListGymsResponse> listGyms() {
        ListGymsResponse response = new ListGymsResponse();
        response.setGyms(gymRepository.findAllGymEntries()); // DTO projections
        return ResponseEntity.ok(response);
    }
}

