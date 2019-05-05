package com.adriangalende.padelHub.repository;

import com.adriangalende.padelHub.entity.PreciosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository("repositorio_precios")
public interface PreciosRepository extends JpaRepository<PreciosEntity, Serializable> {

    @Query(value="from PreciosEntity p WHERE p.idPista = :idPista and p.idTipoUsuario = :idTipoUsuario and p.franjaHoraria = :franjaHoraria")
    public abstract PreciosEntity recuperaPrecio(@Param("idPista") int idPista, @Param("idTipoUsuario") int idTipoUsuario, @Param("franjaHoraria") String franjaHoraria);

    @Query(value="from PreciosEntity p")
    public abstract List<PreciosEntity> todos();

}
