package com.pimpo.pimpofilms.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Film.
 */

@Document(collection = "film")
public class Film implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Size(max = 100)
    @Field("title")
    private String title;

    @NotNull
    @Min(value = 1900)
    @Max(value = 3000)
    @Field("year")
    private Integer year;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
