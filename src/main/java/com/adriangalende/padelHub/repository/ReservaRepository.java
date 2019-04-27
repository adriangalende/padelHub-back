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

    //select * from reserva
    //where
    //    (hora_inicio between '2019-04-23 17:30:00' AND '2019-04-23 18:00:00' or hora_fin between '2019-04-23 16:30:00' AND '2019-04-23 18:00:00')
    //    AND
    //    hora_inicio < '2019-04-23 18:00:00' ;
    @Query(value="from ReservaEntity reserva WHERE (reserva.horaInicio BETWEEN :horaInicio AND :horaFin OR reserva.horaFin BETWEEN :horaInicio AND :horaFin) AND reserva.horaInicio < :horaFin")
    public abstract List<ReservaEntity> getAllBetweenDates(@Param("horaInicio") Date horaInicio, @Param("horaFin") Date horaFin);
}
