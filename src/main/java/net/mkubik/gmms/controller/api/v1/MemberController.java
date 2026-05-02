package net.mkubik.gmms.controller.api.v1;

import lombok.RequiredArgsConstructor;
import net.mkubik.gmms.api.MemberApi;
import net.mkubik.gmms.api.model.CancelMembershipResponse;
import net.mkubik.gmms.api.model.ListMembersResponse;
import net.mkubik.gmms.api.model.RegisterMemberRequest;
import net.mkubik.gmms.api.model.RegisterMemberResponse;
import net.mkubik.gmms.mapper.MemberMapper;
import net.mkubik.gmms.model.Member;
import net.mkubik.gmms.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
    public ResponseEntity<ListMembersResponse> listMembers() {
        List<Member> members = memberService.listMembers();
        ListMembersResponse response = new ListMembersResponse(
                memberMapper.toEntryList(members)
        );
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CancelMembershipResponse> cancelMembership(UUID memberId) {
        Member cancelled = memberService.cancelMembership(memberId);
        return ResponseEntity.ok(memberMapper.toCancelResponse(cancelled));
    }
}
