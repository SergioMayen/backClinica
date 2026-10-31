package com.example.backDocker.repository;

import com.example.backDocker.model.Receta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecetaRepository extends JpaRepository<Receta, Long> {}
