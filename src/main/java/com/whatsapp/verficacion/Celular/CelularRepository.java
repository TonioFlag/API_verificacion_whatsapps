package com.whatsapp.verficacion.Celular;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CelularRepository extends JpaRepository<Celular, Long>{
    Optional<Celular> findByCelular(String Celular);
}
