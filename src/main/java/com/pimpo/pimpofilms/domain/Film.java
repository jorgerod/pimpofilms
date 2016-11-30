package com.pimpo.pimpofilms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Film.
 */
@Entity
@Table(name = "film")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Film implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2100)
    @Column(name = "year", nullable = false)
    private Integer year;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "film_actor",
               joinColumns = @JoinColumn(name="films_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="actors_id", referencedColumnName="ID"))
    private Set<Actor> actors = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Film title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public Film year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public Film actors(Set<Actor> actors) {
        this.actors = actors;
        return this;
    }

    public Film addActor(Actor actor) {
        actors.add(actor);
        actor.getFilms().add(this);
        return this;
    }

    public Film removeActor(Actor actor) {
        actors.remove(actor);
        actor.getFilms().remove(this);
        return this;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Film film = (Film) o;
        if(film.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, film.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Film{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", year='" + year + "'" +
            '}';
    }
}
