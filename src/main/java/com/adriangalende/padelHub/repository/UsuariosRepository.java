package com.adriangalende.padelHub.repository;

import com.adriangalende.padelHub.entity.UsuariosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository("repositorio_usuarios")
public interface UsuariosRepository extends JpaRepository<UsuariosEntity, Serializable> {

    public abstract UsuariosEntity findByEmailAndTelefono(String email, String telefono);

    public abstract List<UsuariosEntity> findByEmailOrTelefono(String email, String telefono);

    @Query("from UsuariosEntity u WHERE u.email = :email or u.telefono = :telefono")
    public abstract Optional<UsuariosEntity> buscarUsuario(@Param("email") String email, @Param("telefono") String telefono);

    public abstract Optional<UsuariosEntity> findById(int id);
}
