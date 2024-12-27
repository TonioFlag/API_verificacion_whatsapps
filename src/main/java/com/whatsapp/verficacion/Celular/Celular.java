package com.whatsapp.verficacion.Celular;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Celular {
    @Id
    @Column(nullable = false, unique = true)
    private String celular;

    @Column(nullable = true)
    private String whatsapp;

    @Column(nullable = false)
    private LocalDate fecha;
}