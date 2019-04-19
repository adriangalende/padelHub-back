package com.adriangalende.padelHub.repository;

import com.adriangalende.padelHub.entity.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository("repositorio_reserva")
public interface ReservaRepository extends JpaRepository<ReservaEntity, Serializable> {
}
