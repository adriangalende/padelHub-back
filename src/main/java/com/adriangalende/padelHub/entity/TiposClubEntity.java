package com.adriangalende.padelHub.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tipos_club", schema = "padelhub", catalog = "")
public class TiposClubEntity {
    private int id;
    private String descripcion;
    private ClubEntity clubById;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "descripcion")
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TiposClubEntity that = (TiposClubEntity) o;
        return id == that.id &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descripcion);
    }

    @OneToOne(mappedBy = "tiposClubById")
    public ClubEntity getClubById() {
        return clubById;
    }

    public void setClubById(ClubEntity clubById) {
        this.clubById = clubById;
    }
}
