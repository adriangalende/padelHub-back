package com.adriangalende.padelHub.entity;

import com.adriangalende.padelHub.model.Usuarios;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "usuarios", schema = "padelhub")
public class UsuariosEntity {
    private int id;
    private String nombre;
    private String telefono;
    private int idTiposUsuario;
    private int idClub;
    private String password;
    private String email;
    private Collection<ReservaEntity> reservasById;
    private TiposUsuarioEntity tiposUsuarioByIdTiposUsuario;
    private ClubEntity clubByIdClub;
    private String token;

    public UsuariosEntity(){ }

    public UsuariosEntity(Usuarios usuario){
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.telefono = usuario.getTelefono();
        this.idTiposUsuario = usuario.getIdTiposUsuario();
        this.idClub = usuario.getIdClub();
        this.password = usuario.getPassword();
        this.email = usuario.getEmail();
    }

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

    @Basic
    @Column(name = "telefono")
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Basic
    @Column(name = "id_tipos_usuario")
    public int getIdTiposUsuario() {
        return idTiposUsuario;
    }

    public void setIdTiposUsuario(int idTiposUsuario) {
        this.idTiposUsuario = idTiposUsuario;
    }

    @Basic
    @Column(name = "id_club")
    public int getIdClub() {
        return idClub;
    }

    public void setIdClub(int idClub) {
        this.idClub = idClub;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "email")
    public String getEmail(){return  email;}

    public void setEmail(String email){ this.email = email; }

    @Basic
    @Column(name="token")
    public String getToken(){return  token;}
    public void setToken(String token){this.token = token;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuariosEntity that = (UsuariosEntity) o;
        return id == that.id &&
                idTiposUsuario == that.idTiposUsuario &&
                idClub == that.idClub &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(telefono, that.telefono) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, telefono, idTiposUsuario, idClub, password);
    }

    @OneToMany(mappedBy = "usuariosByIdUsuario")
    public Collection<ReservaEntity> getReservasById() {
        return reservasById;
    }

    public void setReservasById(Collection<ReservaEntity> reservasById) {
        this.reservasById = reservasById;
    }

    @ManyToOne
    @JoinColumn(name = "id_tipos_usuario", referencedColumnName = "id", nullable = false,insertable = false, updatable = false)
    public TiposUsuarioEntity getTiposUsuarioByIdTiposUsuario() {
        return tiposUsuarioByIdTiposUsuario;
    }

    public void setTiposUsuarioByIdTiposUsuario(TiposUsuarioEntity tiposUsuarioByIdTiposUsuario) {
        this.tiposUsuarioByIdTiposUsuario = tiposUsuarioByIdTiposUsuario;
    }

    @ManyToOne
    @JoinColumn(name = "id_club", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public ClubEntity getClubByIdClub() {
        return clubByIdClub;
    }

    public void setClubByIdClub(ClubEntity clubByIdClub) {
        this.clubByIdClub = clubByIdClub;
    }
}
