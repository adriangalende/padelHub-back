package com.adriangalende.padelHub.repository;

import com.adriangalende.padelHub.entity.PistaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository("repositorio_pista")
public interface PistaRepository extends JpaRepository<PistaEntity, Serializable> {
    public abstract List<PistaEntity> findAll();
}
