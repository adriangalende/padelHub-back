package com.adriangalende.padelHub.repository;

import com.adriangalende.padelHub.entity.TiposUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository("repositorio_tiposUsuario")
public interface TiposUsuarioRepository extends JpaRepository<TiposUsuarioEntity, Serializable> {
}
