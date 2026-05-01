package net.mkubik.gmms.controller.api.v1;

import lombok.RequiredArgsConstructor;
import net.mkubik.gmms.api.MembershipPlanApi;
import net.mkubik.gmms.api.model.CreateMembershipPlanRequest;
import net.mkubik.gmms.api.model.CreateMembershipPlanResponse;
import net.mkubik.gmms.api.model.ListMembershipPlansResponse;
import net.mkubik.gmms.api.model.PageMetadata;
import net.mkubik.gmms.mapper.MembershipPlanMapper;
import net.mkubik.gmms.model.MembershipPlan;
import net.mkubik.gmms.service.MembershipPlanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MembershipPlanController implements MembershipPlanApi {

    private final MembershipPlanService membershipPlanService;
    private final MembershipPlanMapper membershipPlanMapper;

    @Override
    public ResponseEntity<CreateMembershipPlanResponse> createMembershipPlan(
            UUID gymId,
            CreateMembershipPlanRequest request
    ) {
        MembershipPlan plan = membershipPlanMapper.toEntity(request);
        MembershipPlan saved = membershipPlanService.createMembershipPlan(
                gymId, plan, request.getPlanTypeCode()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(membershipPlanMapper.toCreateResponse(saved));
    }

    @Override
    public ResponseEntity<ListMembershipPlansResponse> listMembershipPlans(
            UUID gymId,
            Optional<Integer> page,
            Optional<Integer> size
    ) {
        Page<MembershipPlan> planPage = membershipPlanService.listMembershipPlans(
                gymId, PageRequest.of(page.orElse(0), size.orElse(20))
        );

        PageMetadata pageMetadata = new PageMetadata();
        pageMetadata.setNumber(Optional.of(planPage.getNumber()));
        pageMetadata.setSize(Optional.of(planPage.getSize()));
        pageMetadata.setTotalElements(Optional.of(planPage.getTotalElements()));
        pageMetadata.setTotalPages(Optional.of(planPage.getTotalPages()));

        ListMembershipPlansResponse response = new ListMembershipPlansResponse(
                membershipPlanMapper.toEntryList(planPage.getContent()),
                Optional.of(pageMetadata)
        );
        return ResponseEntity.ok(response);
    }
}
