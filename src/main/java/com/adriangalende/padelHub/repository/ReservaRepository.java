package com.adriangalende.padelHub.repository;

import com.adriangalende.padelHub.entity.ReservaEntity;
import com.adriangalende.padelHub.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository("repositorio_reserva")
public interface ReservaRepository extends JpaRepository<ReservaEntity, Serializable> {

    public abstract List<ReservaEntity> findByHoraInicioOrHoraFin(Date horaInicio, Date horaFin);

    public abstract List<ReservaEntity> findAllByHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(Date horaInicio, Date horaFin);

    @Query(value="from ReservaEntity reserva WHERE (reserva.horaInicio >= :horaInicio or reserva.horaFin > :horaInicio) and reserva.horaInicio < :horaFin ")
    public abstract List<ReservaEntity> getAllBetweenDates(@Param("horaInicio") Date horaInicio, @Param("horaFin") Date horaFin);

    @Query(value="from ReservaEntity reserva WHERE reserva.idPista = :idPista and ((reserva.horaInicio >= :horaInicio or reserva.horaFin > :horaInicio) and reserva.horaInicio < :horaFin )")
    public abstract List<ReservaEntity> recuperarReserva(@Param("idPista") int idPista,@Param("horaInicio") Date horaInicio, @Param("horaFin") Date horaFin);

    @Query(value="from ReservaEntity reserva WHERE reserva.idUsuario = :idUsuario order by reserva.horaInicio desc")
    Optional<List<ReservaEntity>> obtenerReservasUsuario(@Param("idUsuario")int id);


    @Query(value="from ReservaEntity reserva where reserva.idClub = :idClub and DATE_FORMAT(hora_inicio,'%Y-%m-%d %T') >= current_timestamp and DATE_FORMAT(hora_inicio,'%Y-%m-%d') = current_date order by hora_inicio asc")
    Optional<List<ReservaEntity>> obtenerReservasClub(@Param("idClub")int id);


    @Query(value="from ReservaEntity reserva where reserva.idClub = :idClub order by reserva.horaInicio asc")
    Optional<List<ReservaEntity>> obtenerTodasReservasClub(@Param("idClub")int id);
}
