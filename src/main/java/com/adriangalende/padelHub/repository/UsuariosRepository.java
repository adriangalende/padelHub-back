package com.adriangalende.padelHub.repository;

import com.adriangalende.padelHub.entity.UsuariosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository("repositorio_usuarios")
public interface UsuariosRepository extends JpaRepository<UsuariosEntity, Serializable> {
}
