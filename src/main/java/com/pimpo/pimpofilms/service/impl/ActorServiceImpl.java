package com.pimpo.pimpofilms.service.impl;

import com.pimpo.pimpofilms.service.ActorService;
import com.pimpo.pimpofilms.domain.Actor;
import com.pimpo.pimpofilms.repository.ActorRepository;
import com.pimpo.pimpofilms.service.dto.ActorDTO;
import com.pimpo.pimpofilms.service.mapper.ActorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Actor.
 */
@Service
@Transactional
public class ActorServiceImpl implements ActorService{

    private final Logger log = LoggerFactory.getLogger(ActorServiceImpl.class);
    
    @Inject
    private ActorRepository actorRepository;

    @Inject
    private ActorMapper actorMapper;

    /**
     * Save a actor.
     *
     * @param actorDTO the entity to save
     * @return the persisted entity
     */
    public ActorDTO save(ActorDTO actorDTO) {
        log.debug("Request to save Actor : {}", actorDTO);
        Actor actor = actorMapper.actorDTOToActor(actorDTO);
        actor = actorRepository.save(actor);
        ActorDTO result = actorMapper.actorToActorDTO(actor);
        return result;
    }

    /**
     *  Get all the actors.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ActorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Actors");
        Page<Actor> result = actorRepository.findAll(pageable);
        return result.map(actor -> actorMapper.actorToActorDTO(actor));
    }

    /**
     *  Get one actor by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ActorDTO findOne(Long id) {
        log.debug("Request to get Actor : {}", id);
        Actor actor = actorRepository.findOne(id);
        ActorDTO actorDTO = actorMapper.actorToActorDTO(actor);
        return actorDTO;
    }

    /**
     *  Delete the  actor by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Actor : {}", id);
        actorRepository.delete(id);
    }
}
