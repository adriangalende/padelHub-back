package com.adriangalende.padelHub.repository;

import com.adriangalende.padelHub.entity.TiposPistaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository("repositorio_tiposPista")
public interface TiposPistaRepository extends JpaRepository<TiposPistaEntity, Serializable> {
}
