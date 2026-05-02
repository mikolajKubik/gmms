package net.mkubik.gmms.mapper;

import net.mkubik.gmms.api.model.CancelMembershipResponse;
import net.mkubik.gmms.api.model.MemberEntry;
import net.mkubik.gmms.api.model.RegisterMemberRequest;
import net.mkubik.gmms.api.model.RegisterMemberResponse;
import net.mkubik.gmms.model.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    default <T> Optional<T> wrap(T value) {
        return Optional.ofNullable(value);
    }

    default Optional<net.mkubik.gmms.api.model.MembershipStatus> mapStatus(
            net.mkubik.gmms.model.MembershipStatus status
    ) {
        if (status == null) {
            return Optional.empty();
        }
        return Optional.of(
                net.mkubik.gmms.api.model.MembershipStatus.fromValue(status.name())
        );
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "membershipPlan", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    Member toEntity(RegisterMemberRequest request);

    @Mapping(source = "membershipPlan.name", target = "planName")
//    @Mapping(target = "gymName", ignore = true)
    RegisterMemberResponse toRegisterResponse(Member member);

    CancelMembershipResponse toCancelResponse(Member member);

    @Mapping(source = "membershipPlan.name", target = "planName")
    @Mapping(source = "membershipPlan.gym.name", target = "gymName")
    MemberEntry toEntry(Member member);

    List<MemberEntry> toEntryList(List<Member> members);
}
