package net.mkubik.gmms.controller.api.v1;

import net.mkubik.gmms.api.DefaultApi;
import net.mkubik.gmms.api.model.CreateGymRequest;
import net.mkubik.gmms.api.model.CreateGymResponse;
import net.mkubik.gmms.api.model.ListGymsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GymController implements DefaultApi {

    @Override
    public ResponseEntity<CreateGymResponse> createGym(CreateGymRequest createGymRequest) {
        // TODO:
        CreateGymResponse response = new CreateGymResponse();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<ListGymsResponse> listGyms() {
        // TODO:
        ListGymsResponse response = new ListGymsResponse();
        return ResponseEntity.ok(response);
    }
}

