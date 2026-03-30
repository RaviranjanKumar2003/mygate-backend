package com.example.demo.Repositories;

import com.example.demo.Entities.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDataRepository extends JpaRepository<FileData, Long> {



}