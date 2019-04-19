package com.adriangalende.padelHub.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "tipos_usuario", schema = "padelhub", catalog = "")
public class TiposUsuarioEntity {
    private int id;
    private String nombre;
    private Collection<UsuariosEntity> usuariosById;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "nombre")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TiposUsuarioEntity that = (TiposUsuarioEntity) o;
        return id == that.id &&
                Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }

    @OneToMany(mappedBy = "tiposUsuarioByIdTiposUsuario")
    public Collection<UsuariosEntity> getUsuariosById() {
        return usuariosById;
    }

    public void setUsuariosById(Collection<UsuariosEntity> usuariosById) {
        this.usuariosById = usuariosById;
    }
}
