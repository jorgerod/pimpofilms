package com.pimpo.pimpofilms.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Film entity.
 */
public class FilmDTO implements Serializable {

    private String id;

    @Size(max = 100)
    private String title;

    @NotNull
    @Min(value = 1900)
    @Max(value = 3000)
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

    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getYear() {
        return year;
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

        FilmDTO filmDTO = (FilmDTO) o;

        if ( ! Objects.equals(id, filmDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FilmDTO{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", year='" + year + "'" +
            '}';
    }
}
