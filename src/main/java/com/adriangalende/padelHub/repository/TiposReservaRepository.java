package com.adriangalende.padelHub.repository;

import com.adriangalende.padelHub.entity.TiposReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository("repositorio_TiposReserva")
public interface TiposReservaRepository extends JpaRepository<TiposReservaEntity, Serializable> {
}
