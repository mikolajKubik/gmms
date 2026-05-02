package net.mkubik.gmms.service;

import net.mkubik.gmms.model.Member;

import java.util.List;
import java.util.UUID;

public interface MemberService {

    Member registerMember(UUID planId, Member member);

    List<Member> listMembers();

    Member cancelMembership(UUID memberId);
}