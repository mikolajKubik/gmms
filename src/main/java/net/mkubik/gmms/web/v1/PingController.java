package net.mkubik.gmms.web.v1;

import net.mkubik.gmms.api.PingApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController implements PingApi {

    @Override
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}
