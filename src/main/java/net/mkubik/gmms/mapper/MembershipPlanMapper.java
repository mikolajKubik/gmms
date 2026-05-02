package net.mkubik.gmms.mapper;

import net.mkubik.gmms.api.model.CreateMembershipPlanRequest;
import net.mkubik.gmms.api.model.CreateMembershipPlanResponse;
import net.mkubik.gmms.api.model.MembershipPlanEntry;
import net.mkubik.gmms.model.MembershipPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface MembershipPlanMapper {

    default <T> Optional<T> wrap(T value) {
        return Optional.ofNullable(value);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "gym", ignore = true)
    @Mapping(target = "planType", ignore = true)
    @Mapping(target = "activeMembers", ignore = true)
    MembershipPlan toEntity(CreateMembershipPlanRequest request);

    @Mapping(source = "planType.code", target = "planTypeCode")
    CreateMembershipPlanResponse toCreateResponse(MembershipPlan plan);

    @Mapping(source = "planType.code", target = "planTypeCode")
    MembershipPlanEntry toEntry(MembershipPlan plan);

    List<MembershipPlanEntry> toEntryList(List<MembershipPlan> plans);
}
