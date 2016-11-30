package com.pimpo.pimpofilms.web.rest;

import com.pimpo.pimpofilms.PimpofilmsApp;

import com.pimpo.pimpofilms.domain.Film;
import com.pimpo.pimpofilms.repository.FilmRepository;
import com.pimpo.pimpofilms.service.FilmService;
import com.pimpo.pimpofilms.service.dto.FilmDTO;
import com.pimpo.pimpofilms.service.mapper.FilmMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FilmResource REST controller.
 *
 * @see FilmResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PimpofilmsApp.class)
public class FilmResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR = 1900;
    private static final Integer UPDATED_YEAR = 1901;

    @Inject
    private FilmRepository filmRepository;

    @Inject
    private FilmMapper filmMapper;

    @Inject
    private FilmService filmService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFilmMockMvc;

    private Film film;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FilmResource filmResource = new FilmResource();
        ReflectionTestUtils.setField(filmResource, "filmService", filmService);
        this.restFilmMockMvc = MockMvcBuilders.standaloneSetup(filmResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Film createEntity(EntityManager em) {
        Film film = new Film()
                .title(DEFAULT_TITLE)
                .year(DEFAULT_YEAR);
        return film;
    }

    @Before
    public void initTest() {
        film = createEntity(em);
    }

    @Test
    @Transactional
    public void createFilm() throws Exception {
        int databaseSizeBeforeCreate = filmRepository.findAll().size();

        // Create the Film
        FilmDTO filmDTO = filmMapper.filmToFilmDTO(film);

        restFilmMockMvc.perform(post("/api/films")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(filmDTO)))
                .andExpect(status().isCreated());

        // Validate the Film in the database
        List<Film> films = filmRepository.findAll();
        assertThat(films).hasSize(databaseSizeBeforeCreate + 1);
        Film testFilm = films.get(films.size() - 1);
        assertThat(testFilm.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFilm.getYear()).isEqualTo(DEFAULT_YEAR);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = filmRepository.findAll().size();
        // set the field null
        film.setTitle(null);

        // Create the Film, which fails.
        FilmDTO filmDTO = filmMapper.filmToFilmDTO(film);

        restFilmMockMvc.perform(post("/api/films")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(filmDTO)))
                .andExpect(status().isBadRequest());

        List<Film> films = filmRepository.findAll();
        assertThat(films).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = filmRepository.findAll().size();
        // set the field null
        film.setYear(null);

        // Create the Film, which fails.
        FilmDTO filmDTO = filmMapper.filmToFilmDTO(film);

        restFilmMockMvc.perform(post("/api/films")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(filmDTO)))
                .andExpect(status().isBadRequest());

        List<Film> films = filmRepository.findAll();
        assertThat(films).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFilms() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the films
        restFilmMockMvc.perform(get("/api/films?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(film.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    @Test
    @Transactional
    public void getFilm() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get the film
        restFilmMockMvc.perform(get("/api/films/{id}", film.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(film.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    @Transactional
    public void getNonExistingFilm() throws Exception {
        // Get the film
        restFilmMockMvc.perform(get("/api/films/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFilm() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);
        int databaseSizeBeforeUpdate = filmRepository.findAll().size();

        // Update the film
        Film updatedFilm = filmRepository.findOne(film.getId());
        updatedFilm
                .title(UPDATED_TITLE)
                .year(UPDATED_YEAR);
        FilmDTO filmDTO = filmMapper.filmToFilmDTO(updatedFilm);

        restFilmMockMvc.perform(put("/api/films")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(filmDTO)))
                .andExpect(status().isOk());

        // Validate the Film in the database
        List<Film> films = filmRepository.findAll();
        assertThat(films).hasSize(databaseSizeBeforeUpdate);
        Film testFilm = films.get(films.size() - 1);
        assertThat(testFilm.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFilm.getYear()).isEqualTo(UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void deleteFilm() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);
        int databaseSizeBeforeDelete = filmRepository.findAll().size();

        // Get the film
        restFilmMockMvc.perform(delete("/api/films/{id}", film.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Film> films = filmRepository.findAll();
        assertThat(films).hasSize(databaseSizeBeforeDelete - 1);
    }
}
