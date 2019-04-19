package com.adriangalende.padelHub.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "tipos_pista", schema = "padelhub", catalog = "")
public class TiposPistaEntity {
    private int id;
    private String descripcion;
    private Collection<PistaEntity> pistasById;

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
        TiposPistaEntity that = (TiposPistaEntity) o;
        return id == that.id &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descripcion);
    }

    @OneToMany(mappedBy = "tiposPistaByIdTipoPista")
    public Collection<PistaEntity> getPistasById() {
        return pistasById;
    }

    public void setPistasById(Collection<PistaEntity> pistasById) {
        this.pistasById = pistasById;
    }
}
