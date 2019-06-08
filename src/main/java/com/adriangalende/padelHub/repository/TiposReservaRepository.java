package com.adriangalende.padelHub.repository;

import com.adriangalende.padelHub.entity.TiposReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository("repositorio_TiposReserva")
public interface TiposReservaRepository extends JpaRepository<TiposReservaEntity, Serializable> {
    @Query("FROM TiposReservaEntity tr where tr.idClub = :idClub")
    Optional<List<TiposReservaEntity>> obtenerTiposReservaClub(@Param("idClub")int idClub);
}
