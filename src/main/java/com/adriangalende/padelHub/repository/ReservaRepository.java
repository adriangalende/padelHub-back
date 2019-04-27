package com.adriangalende.padelHub.repository;

import com.adriangalende.padelHub.entity.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Repository("repositorio_reserva")
public interface ReservaRepository extends JpaRepository<ReservaEntity, Serializable> {

    public abstract List<ReservaEntity> findByHoraInicioOrHoraFin(Date horaInicio, Date horaFin);

    public abstract List<ReservaEntity> findAllByHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(Date horaInicio, Date horaFin);

    @Query(value="from ReservaEntity reserva WHERE (reserva.horaInicio >= :horaInicio or reserva.horaFin > :horaInicio) and reserva.horaInicio < :horaFin ")
    public abstract List<ReservaEntity> getAllBetweenDates(@Param("horaInicio") Date horaInicio, @Param("horaFin") Date horaFin);
}
