package com.adriangalende.padelHub.repository;

import com.adriangalende.padelHub.entity.ClubEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository("repositorio_club")
public interface ClubRepository extends JpaRepository<ClubEntity, Serializable> {
}
