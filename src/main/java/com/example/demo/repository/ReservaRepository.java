package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Reserva;

@Repository("reservaRepository")
public interface ReservaRepository extends JpaRepository<Reserva, Long>{

}
