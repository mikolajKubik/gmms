package net.mkubik.gmms.mapper;

import net.mkubik.gmms.api.model.CreateGymRequest;
import net.mkubik.gmms.api.model.CreateGymResponse;
import net.mkubik.gmms.model.Gym;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GymMapper {

    Gym toEntity(CreateGymRequest request);

    CreateGymResponse toCreateResponse(Gym gym);
}
