package net.mkubik.gmms.service;

import net.mkubik.gmms.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MemberService {

    Member registerMember(UUID planId, Member member);

    Page<Member> listMembers(Pageable pageable);

    Member cancelMembership(UUID memberId);
}