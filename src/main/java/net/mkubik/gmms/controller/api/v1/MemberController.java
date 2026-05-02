package net.mkubik.gmms.controller.api.v1;

import lombok.RequiredArgsConstructor;
import net.mkubik.gmms.api.MemberApi;
import net.mkubik.gmms.api.model.CancelMembershipResponse;
import net.mkubik.gmms.api.model.ListMembersResponse;
import net.mkubik.gmms.api.model.PageMetadata;
import net.mkubik.gmms.api.model.RegisterMemberRequest;
import net.mkubik.gmms.api.model.RegisterMemberResponse;
import net.mkubik.gmms.mapper.MemberMapper;
import net.mkubik.gmms.model.Member;
import net.mkubik.gmms.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MemberController implements MemberApi {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @Override
    public ResponseEntity<RegisterMemberResponse> registerMember(
            UUID planId,
            RegisterMemberRequest registerMemberRequest
    ) {
        Member member = memberMapper.toEntity(registerMemberRequest);
        Member saved = memberService.registerMember(planId, member);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberMapper.toRegisterResponse(saved));
    }

    @Override
    public ResponseEntity<ListMembersResponse> listMembers(
            Optional<Integer> page,
            Optional<Integer> size
    ) {
        Page<Member> memberPage = memberService.listMembers(
                PageRequest.of(page.orElse(0), size.orElse(20))
        );

        PageMetadata pageMetadata = new PageMetadata();
        pageMetadata.setNumber(Optional.of(memberPage.getNumber()));
        pageMetadata.setSize(Optional.of(memberPage.getSize()));
        pageMetadata.setTotalElements(Optional.of(memberPage.getTotalElements()));
        pageMetadata.setTotalPages(Optional.of(memberPage.getTotalPages()));

        ListMembersResponse response = new ListMembersResponse(
                memberMapper.toEntryList(memberPage.getContent()),
                Optional.of(pageMetadata)
        );
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CancelMembershipResponse> cancelMembership(UUID memberId) {
        Member cancelled = memberService.cancelMembership(memberId);
        return ResponseEntity.ok(memberMapper.toCancelResponse(cancelled));
    }
}
