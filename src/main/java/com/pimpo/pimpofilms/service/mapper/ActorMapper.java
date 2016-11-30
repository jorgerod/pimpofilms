package com.pimpo.pimpofilms.service.mapper;

import com.pimpo.pimpofilms.domain.*;
import com.pimpo.pimpofilms.service.dto.ActorDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Actor and its DTO ActorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ActorMapper {

    ActorDTO actorToActorDTO(Actor actor);

    List<ActorDTO> actorsToActorDTOs(List<Actor> actors);

    @Mapping(target = "films", ignore = true)
    Actor actorDTOToActor(ActorDTO actorDTO);

    List<Actor> actorDTOsToActors(List<ActorDTO> actorDTOs);
}
