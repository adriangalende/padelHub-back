package com.adriangalende.padelHub.repository;

import com.adriangalende.padelHub.entity.UsuariosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository("repositorio_usuarios")
public interface UsuariosRepository extends JpaRepository<UsuariosEntity, Serializable> {

    public abstract UsuariosEntity findByEmailAndTelefono(String email, String telefono);

    public abstract List<UsuariosEntity> findByEmailOrTelefono(String email, String telefono);

    public abstract Optional<UsuariosEntity> findById(int id);
}
