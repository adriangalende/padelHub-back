package com.adriangalende.padelHub.repository;

import com.adriangalende.padelHub.entity.PreciosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository("repositorio_precios")
public interface PreciosRepository extends JpaRepository<PreciosEntity, Serializable> {
}
