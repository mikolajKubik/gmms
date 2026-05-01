package net.mkubik.gmms.controller.api.v1;

import lombok.RequiredArgsConstructor;
import net.mkubik.gmms.api.DefaultApi;
import net.mkubik.gmms.api.model.CreateGymRequest;
import net.mkubik.gmms.api.model.CreateGymResponse;
import net.mkubik.gmms.api.model.GymEntry;
import net.mkubik.gmms.api.model.ListGymsResponse;
import net.mkubik.gmms.api.model.PageMetadata;
import net.mkubik.gmms.mapper.GymMapper;
import net.mkubik.gmms.model.Gym;
import net.mkubik.gmms.repository.GymRepository;
import net.mkubik.gmms.service.GymService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class GymController implements DefaultApi {

    private final GymService gymService;
    private final GymMapper gymMapper;
    private final GymRepository gymRepository;

    @Override
    public ResponseEntity<CreateGymResponse> createGym(CreateGymRequest createGymRequest) {
        Gym gym = gymMapper.toEntity(createGymRequest);
        Gym saved = gymService.createGym(gym);
        return ResponseEntity.status(HttpStatus.CREATED).body(gymMapper.toCreateResponse(saved));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ListGymsResponse> listGyms(Optional<Integer> page, Optional<Integer> size) {
        Page<GymEntry> gymPage = gymRepository.findAllGymEntries(
                PageRequest.of(page.orElse(0), size.orElse(20)) // TODO: handle 500 on size 0
        );

        PageMetadata pageMetadata = new PageMetadata();
        pageMetadata.setNumber(Optional.of(gymPage.getNumber()));
        pageMetadata.setSize(Optional.of(gymPage.getSize()));
        pageMetadata.setTotalElements(Optional.of(gymPage.getTotalElements()));
        pageMetadata.setTotalPages(Optional.of(gymPage.getTotalPages()));

        ListGymsResponse response = new ListGymsResponse();
        response.setGyms(gymPage.getContent());
        response.setPage(Optional.of(pageMetadata));
        return ResponseEntity.ok(response);
    }
}

