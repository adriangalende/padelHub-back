package com.adriangalende.padelHub.repository;

import com.adriangalende.padelHub.entity.TiposClubEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository("repositorio_tiposClub")
public interface TiposClubRepository extends JpaRepository<TiposClubEntity, Serializable> {
    public abstract List<TiposClubEntity> findAllBy();
}
