package com.naname.fileparser.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO file(name, size, content) VALUES (?1, ?2, ?3)", nativeQuery = true)
    void insertFile(String name, String size, String content);

    @Query(value = "SELECT max(id) FROM file", nativeQuery = true)
    int getLastId();
}
