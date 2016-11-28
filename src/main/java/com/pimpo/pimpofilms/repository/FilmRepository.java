package com.pimpo.pimpofilms.repository;

import com.pimpo.pimpofilms.domain.Film;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Film entity.
 */
@SuppressWarnings("unused")
public interface FilmRepository extends MongoRepository<Film,String> {

}
