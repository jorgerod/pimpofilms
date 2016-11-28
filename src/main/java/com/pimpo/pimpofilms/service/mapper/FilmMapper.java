package com.pimpo.pimpofilms.service.mapper;

import com.pimpo.pimpofilms.domain.*;
import com.pimpo.pimpofilms.service.dto.FilmDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Film and its DTO FilmDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FilmMapper {

    FilmDTO filmToFilmDTO(Film film);

    List<FilmDTO> filmsToFilmDTOs(List<Film> films);

    Film filmDTOToFilm(FilmDTO filmDTO);

    List<Film> filmDTOsToFilms(List<FilmDTO> filmDTOs);
}
